package com.chua.utils.netx.lucene.operator;

import com.chua.utils.netx.lucene.entity.DataDocument;
import com.chua.utils.netx.lucene.entity.HitData;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.constant.NumberConstant;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.SortField.Type;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_MINS;

/**
 * 查询模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/3
 */
@Slf4j
@AllArgsConstructor
public class DefaultSearchOperatorTemplate implements SearchOperatorTemplate {

    /**
     * 索引模板
     */
    private IndexOperatorTemplate indexOperatorTemplate;
    /**
     * 索引
     */
    private String index;

    @Override
    public HitData search(String keyword, String sort, String columns, int offset, int pageSize) {
        if (!indexOperatorTemplate.exist(index)) {
            throw new IllegalStateException("索引不存在");
        }
        long startTime = System.currentTimeMillis();
        //获取查询器
        List<IndexReader> indexReaderList = indexOperatorTemplate.indexReader(index);

        ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor(indexReaderList.size());
        final int newPageSize = (pageSize < 0 || pageSize > 1000 ? 1000 : pageSize);
        //当前页
        int endPage = offset * newPageSize;
        //当前页
        int startPage = (offset - 1) * newPageSize;
        //排序
        Sort sort1 = getSort(sort);

        final LongAdder longAdder = new LongAdder();

        List<Future<List<Map<String, Object>>>> futureList = new ArrayList<>();
        for (IndexReader indexReader : indexReaderList) {
            futureList.add(executorService.submit(new Callable<List<Map<String, Object>>>() {
                @Override
                public List<Map<String, Object>> call() throws Exception {
                    // 1 创建查询解析器对象
                    // 参数一:默认的搜索域, 参数二:使用的分析器
                    StandardQueryParser queryParser = new StandardQueryParser(indexOperatorTemplate.getAnalyzer());
                    // 支持后缀匹配，如*国 则可以搜索中国、美国等以国字结尾的词，*:*可以查询所有索引
                    queryParser.setAllowLeadingWildcard(true);
                    // 2 使用查询解析器对象, 实例化Query对象
                    Query query = queryParser.parse(keyword, DataDocument.UNIQUELY_IDENTIFIES);

                    // 3. 创建索引搜索对象(IndexSearcher), 用于执行索引
                    IndexSearcher searcher = new IndexSearcher(indexReader);
                    // 4. 使用IndexSearcher对象执行搜索, 返回搜索结果集TopDocs
                    // 参数一:使用的查询对象, 参数二:前n个
                    TopDocs topDocs = null;
                    if (null != sort1) {
                        topDocs = searcher.search(query, endPage, sort1);
                    } else {
                        topDocs = searcher.search(query, endPage);
                    }
                    longAdder.add(topDocs.totalHits.value);
                    List<Map<String, Object>> data = new ArrayList<>();
                    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
                    int length = scoreDocs.length;
                    for (int i = 0; i < length; i++) {
                        data.add(toMap(searcher, scoreDocs[i], columns));
                    }
                    return data;
                }
            }));
        }

        List<Map<String, Object>> result = getThreadPoolData(futureList);
        HitData hitData = new HitData();
        hitData.setData(result.size() < newPageSize ? result : result.subList(0, newPageSize));
        hitData.setHits(longAdder.longValue());
        hitData.setTotal(hitData.getData().size());
        try {
            return hitData;
        } finally {
            int size = result.size();
            log.info("数据查询耗时{}ms, 表达式: {}, 结果数量： {}/{}", System.currentTimeMillis() - startTime, keyword, size < pageSize ? size : pageSize, size);
        }
    }

    @Override
    public HitData quickSearch(String keyword, String sort, String columns, int offset, int pageSize) {
        if (!indexOperatorTemplate.exist(index)) {
            throw new IllegalStateException("索引不存在");
        }
        long startTime = System.currentTimeMillis();
        //获取查询器
        List<IndexReader> indexReaderList = indexOperatorTemplate.indexReader(index);
        int size = indexReaderList.size();
        int thread = Math.min(size, Runtime.getRuntime().availableProcessors() * 2);
        //结果集
        final List<Map<String, Object>> data = new ArrayList<>();
        ExecutorService executorService = ThreadHelper.newFixedThreadExecutor(thread);
        final int newPageSize = (pageSize < 0 || pageSize > 1000 ? 1000 : pageSize);
        //当前页
        int endPage = offset * newPageSize;
        //当前页
        int startPage = (offset - 1) * newPageSize;
        //计数器
        final LongAdder longAdder = new LongAdder();
        //hit计数器
        final LongAdder hitAdder = new LongAdder();
        //排序
        Sort sort1 = getSort(sort);
        CountDownLatch countDownLatch = new CountDownLatch(thread);
        List<Future<List<Map<String, Object>>>> futureList = new ArrayList<>();
        for (IndexReader indexReader : indexReaderList) {
            int finalPageSize = pageSize;
            executorService.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    if (longAdder.intValue() >= newPageSize) {
                        countDownLatch.countDown();
                        return;
                    }
                    // 1 创建查询解析器对象
                    // 参数一:默认的搜索域, 参数二:使用的分析器
                    StandardQueryParser queryParser = new StandardQueryParser(indexOperatorTemplate.getAnalyzer());
                    // 支持后缀匹配，如*国 则可以搜索中国、美国等以国字结尾的词，*:*可以查询所有索引
                    queryParser.setAllowLeadingWildcard(true);
                    // 2 使用查询解析器对象, 实例化Query对象
                    Query query = queryParser.parse(keyword, DataDocument.UNIQUELY_IDENTIFIES);

                    // 3. 创建索引搜索对象(IndexSearcher), 用于执行索引
                    IndexSearcher searcher = new IndexSearcher(indexReader);
                    // 4. 使用IndexSearcher对象执行搜索, 返回搜索结果集TopDocs
                    // 参数一:使用的查询对象, 参数二:指定要返回的搜索结果排序后的前n个
                    TopDocs topDocs = null;
                    if (null != sort1) {
                        topDocs = searcher.search(query, endPage, sort1);
                    } else {
                        topDocs = searcher.search(query, endPage);
                    }
                    hitAdder.add(topDocs.totalHits.value);
                    for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                        if (longAdder.intValue() >= newPageSize) {
                            countDownLatch.countDown();
                            break;
                        }
                        data.add(toMap(searcher, scoreDoc, columns));
                        longAdder.increment();
                    }
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int size1 = data.size();
        HitData hitData = new HitData();
        hitData.setData(size1 > newPageSize ? data.subList(0, newPageSize) : data);
        hitData.setHits(hitAdder.longValue());
        hitData.setTotal(longAdder.intValue());
        try {
            return hitData;
        } finally {
            log.info("数据查询耗时{}ms, 表达式: {}", System.currentTimeMillis() - startTime, keyword);
        }
    }

    /**
     * 获取线程池数据
     *
     * @param futureList 线程池
     * @return List
     */
    private List<Map<String, Object>> getThreadPoolData(List<Future<List<Map<String, Object>>>> futureList) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Future<List<Map<String, Object>>> future : futureList) {
            List<Map<String, Object>> mapList = null;
            try {
                mapList = future.get();
            } catch (Throwable e) {
                continue;
            }
            result.addAll(mapList);
        }
        return result;
    }

    /**
     * 获取Map
     *
     * @param searcher 查询器
     * @param scoreDoc 结果
     * @param columns  字段
     * @return
     */
    private Map<String, Object> toMap(IndexSearcher searcher, ScoreDoc scoreDoc, String columns) {
        Document document = null;
        try {
            document = searcher.doc(scoreDoc.doc);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
        Map<String, Object> map = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        if (null == columns || columns.isEmpty()) {
            List<IndexableField> documentFields = document.getFields();
            for (IndexableField indexableField : documentFields) {
                map.put(indexableField.name(), indexableField.stringValue());
            }
        } else {
            Document finalDocument = document;
            Splitter.on(",").trimResults().omitEmptyStrings().splitToList(columns).forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    String s1 = finalDocument.get(s);
                    map.put(s, s1);
                }
            });
        }
        return map;
    }

    /**
     * 获取排序舒心
     *
     * @param sort 排序
     * @return
     */
    private Sort getSort(String sort) {
        if (Strings.isNullOrEmpty(sort)) {
            return null;
        }
        Sort sort1 = new Sort();

        List<SortField> sortFieldList = new ArrayList<>();
        sort1.setSort(sortFieldList.toArray(new SortField[0]));
        Splitter.on(",").omitEmptyStrings().trimResults().splitToList(sort).parallelStream().forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                String name = s.trim();
                if (s.startsWith(SYMBOL_MINS)) {
                    name = s.substring(1);
                }
                SortField sortField = new SortField(name, Type.LONG);
                sortFieldList.add(sortField);
            }
        });

        return sort1;
    }
}

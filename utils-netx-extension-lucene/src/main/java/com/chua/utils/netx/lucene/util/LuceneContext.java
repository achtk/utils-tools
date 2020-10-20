package com.chua.utils.netx.lucene.util;

import com.google.common.base.Splitter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Lucene工具
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/20 17:49
 */
@RequiredArgsConstructor
@AllArgsConstructor
public class LuceneContext {
    /**
     * 存储目录
     */
    @NonNull
    private Directory directory;

    /**
     *
     */
    private IndexWriter indexWriter;
    /**
     * 分词器
     */
    private Analyzer analyzer;

    {
        try {
            /*
             * 内存索引库特点：
             * 		访问数据的速度比较快
             * 		数据只是暂时的
             */
            //directory = RAMDirectory.open(); //内存数据存取目录
            analyzer = new StandardAnalyzer();
            // 分词器
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, indexWriterConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除以前的index
     *
     * @throws IOException
     */
    public void deleteAll() throws IOException {
        indexWriter.deleteAll();
    }

    /**
     * 精确查询
     *
     * @param keyWord
     */
    public List<Map<String, Object>> termSearch(String keyWord) throws ParseException, IOException {
        //精确查询，根据名称来直接
        List<String> strings = Splitter.on(":").splitToList(keyWord);
        Query query = new TermQuery(new Term(strings.get(0), strings.get(1)));
        return search(query);
    }

    /**
     * 前缀查询
     *
     * @param keyWord
     * @return
     */
    public List<Map<String, Object>> prefixSearch(String keyWord) throws IOException {
        List<String> strings = Splitter.on(":").splitToList(keyWord);
        //查询前缀 邮箱 以z开头的数据
        Query query = new PrefixQuery(new Term(strings.get(0), strings.get(1)));
        return search(query);
    }

    /**
     * 结尾查询
     *
     * @param keyWord
     * @return
     */
    public List<Map<String, Object>> wildcardSearch(String keyWord) throws IOException {
        List<String> strings = Splitter.on(":").splitToList(keyWord);
        //查询前缀 邮箱 以z开头的数据
        Query query = new WildcardQuery(new Term(strings.get(0), strings.get(1)));
        return search(query);
    }
    /**
     * 模糊查询
     *
     * @param keyWord
     * @return
     */
    public List<Map<String, Object>> fuzzySearch(String keyWord) throws IOException {
        List<String> strings = Splitter.on(":").splitToList(keyWord);
        //查询前缀 邮箱 以z开头的数据
        Query query = new FuzzyQuery(new Term(strings.get(0), strings.get(1)));
        return search(query);
    }

    /**
     * 查询数据
     *
     * @param keyWord
     */
    public List<Map<String, Object>> multiSearch(String keyWord) throws ParseException, IOException {
        //简单的查询，创建Query表示搜索域为content包含keyWord的文档
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        while (true) {
            if(keyWord.length() == 0) {
                break;
            }
            int index = keyWord.indexOf("and");
            if(index == -1) {
                int orIndex = keyWord.indexOf("or");
                if(orIndex == -1) {
                    List<String> strings = Splitter.on(":").splitToList(keyWord);
                    String key = strings.get(0);
                    String value = strings.get(1);
                    renderBuilder(builder, key, value);
                } else {
                    String substring = keyWord.substring(0, orIndex);
                    List<String> strings = Splitter.on(":").splitToList(substring);
                    String key = strings.get(0);
                    String value = strings.get(1);
                    renderOrBuilder(builder, key, value);
                    keyWord = keyWord.substring(index);
                }
            }
            String substring = keyWord.substring(0, index);
            List<String> strings = Splitter.on(":").splitToList(substring);
            String key = strings.get(0);
            String value = strings.get(1);
            renderBuilder(builder, key, value);
            keyWord = keyWord.substring(index);
        }
        return search(builder.build());
    }

    private void renderBuilder(BooleanQuery.Builder builder, String key, String value) {
        if(value.indexOf("*") == -1) {
            builder.add(new TermQuery(new Term(key, value)), BooleanClause.Occur.MUST);
        } else if(value.startsWith("*") && !value.endsWith("*")){
            builder.add(new PrefixQuery(new Term(key, value)), BooleanClause.Occur.MUST);
        }  else if(!value.startsWith("*") &&value.endsWith("*")){
            builder.add(new WildcardQuery(new Term(key, value)), BooleanClause.Occur.MUST);
        } else {
            builder.add(new FuzzyQuery(new Term(key, value)), BooleanClause.Occur.MUST);
        }
    }
    private void renderOrBuilder(BooleanQuery.Builder builder, String key, String value) {
        if(value.indexOf("*") == -1) {
            builder.add(new TermQuery(new Term(key, value)), BooleanClause.Occur.SHOULD);
        } else if(value.startsWith("*") && !value.endsWith("*")){
            builder.add(new PrefixQuery(new Term(key, value)), BooleanClause.Occur.SHOULD);
        }  else if(!value.startsWith("*") &&value.endsWith("*")){
            builder.add(new WildcardQuery(new Term(key, value)), BooleanClause.Occur.SHOULD);
        } else {
            builder.add(new FuzzyQuery(new Term(key, value)), BooleanClause.Occur.SHOULD);
        }
    }

    /**
     * 查询数据
     *
     * @param query
     */
    public List<Map<String, Object>> search(Query query) throws IOException {
        DirectoryReader directoryReader = null;
        try {
            // 2、创建IndexReader
            directoryReader = DirectoryReader.open(directory);
            // 3、根据IndexReader创建IndexSearch
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
            // 4、创建搜索的Query
            // 5、根据searcher搜索并且返回TopDocs
            // 搜索前100条结果
            TopDocs topDocs = indexSearcher.search(query, 100);
            // totalHits和scoreDocs.length的区别还没搞明白
            System.out.println("共找到匹配处：" + topDocs.totalHits);
            // 6、根据TopDocs获取ScoreDoc对象
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            System.out.println("共找到匹配文档数：" + scoreDocs.length);
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 7、根据searcher和ScoreDoc对象获取具体的Document对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                return DocumentUtil.toList(document.getFields());
            }
        } finally {
            directoryReader.close();
        }
        return null;
    }

    public void highlight(IndexSearcher indexSearcher, Query query, ScoreDoc[] scoreDocs) throws IOException, InvalidTokenOffsetsException {
        QueryScorer scorer = new QueryScorer(query, "content");

        // 自定义高亮代码
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<span style=\"backgroud:red\">", "</span>");
        Highlighter highlighter = new Highlighter(htmlFormatter, scorer);
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 7、根据searcher和ScoreDoc对象获取具体的Document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("-----------------------------------------");
            System.out.println(document.get("fileName") + ":" + document.get("filePath"));
            System.out.println(highlighter.getBestFragment(analyzer, "content", document.get("content")));
            System.out.println("");
        }
    }
}

package com.chua.utils.netx.lucene.aware;

import com.chua.utils.netx.entity.DocumentData;
import com.chua.utils.netx.entity.DocumentMap;
import com.chua.utils.netx.entity.Search;
import com.chua.utils.netx.function.DocumentContextAware;
import com.chua.utils.netx.lucene.util.DocumentUtil;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BeansHelper;
import com.chua.utils.tools.common.BooleanHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.flexible.core.QueryParserHelper;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

/**
 * lucene
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
@Slf4j
public class NioFSLuceneContextAware implements DocumentContextAware<Map<String, Object>> {

    @Getter
    @Setter
    private Path path;
    private Directory directory;
    private Analyzer analyzer;
    private IndexWriter indexWriter;
    private DirectoryReader indexReader;

    public NioFSLuceneContextAware() {
    }

    public NioFSLuceneContextAware(Path path) throws IOException {
        this(path, new StandardAnalyzer(), new NIOFSDirectory(path));
    }

    public NioFSLuceneContextAware(Path path, Analyzer analyzer) throws IOException {
        this(path, analyzer, new NIOFSDirectory(path));
    }

    public NioFSLuceneContextAware(Path path, Directory directory) throws IOException {
        this(path, new StandardAnalyzer(), directory);
    }

    public NioFSLuceneContextAware(Path path, Analyzer analyzer, Directory directory) throws IOException {
        this.path = path;
        this.analyzer = analyzer;
        this.directory = directory;
        // 分词器
        this.indexWriter = new IndexWriter(directory, new IndexWriterConfig(analyzer));
        if (DirectoryReader.indexExists(directory)) {
            this.indexReader = DirectoryReader.open(directory);
        }
    }

    @Override
    public void addDocument(DocumentMap documentMap) throws Exception {
        Document document = DocumentUtil.map2Document(documentMap);
        indexWriter.addDocument(document);
        indexWriter.commit();
    }

    @Override
    public void addDocuments(List<DocumentMap> documentMaps) throws Exception {
        List<Document> documents = DocumentUtil.map2Documents(documentMaps);
        indexWriter.addDocuments(documents);
        indexWriter.commit();
    }

    @Override
    public Integer deleteDocument(DocumentMap documentMap) {
        return null;
    }

    @Override
    public DocumentData<Map<String, Object>> search(Search search) throws Exception {
        List<String> fields = search.getFields();
        Search.Match searchMatch = search.getMatch();
        if (searchMatch == Search.Match.FIELD && !BooleanHelper.hasLength(fields)) {
            return null;
        }
        if (indexReader == null) {
            synchronized (this) {
                if (!DirectoryReader.indexExists(directory)) {
                    log.warn("索引数据不存在无法查询");
                    return null;
                }
                this.indexReader = DirectoryReader.open(directory);
            }
        }
        // 1 创建查询解析器对象
        // 参数一:默认的搜索域, 参数二:使用的分析器
        StandardQueryParser queryParser = new StandardQueryParser(analyzer);
        // 支持后缀匹配，如*国 则可以搜索中国、美国等以国字结尾的词，*:*可以查询所有索引
        queryParser.setAllowLeadingWildcard(true);
        // 2 使用查询解析器对象, 实例化Query对象
        Query query = queryParser.parse(search.getSearch(), DocumentUtil.CREATE_TIME);
        // 3. 创建索引搜索对象(IndexSearcher), 用于执行索引
        IndexSearcher searcher = new IndexSearcher(indexReader);
        // 4. 使用IndexSearcher对象执行搜索, 返回搜索结果集TopDocs
        // 参数一:使用的查询对象, 参数二:指定要返回的搜索结果排序后的前n个
        TopDocs topDocs = searcher.search(query, search.getMax());

        List<Map<String, Object>> data = new ArrayList<>();
        DocumentData<Map<String, Object>> documentData = new DocumentData<>();

        documentData.setHit(topDocs.totalHits.value);
        documentData.setData(data);

        Arrays.stream(topDocs.scoreDocs).forEach(new Consumer<ScoreDoc>() {
            @Override
            public void accept(ScoreDoc scoreDoc) {
                Document document = null;
                try {
                    document = searcher.doc(scoreDoc.doc);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                Map<String, Object> map = null;
                if (searchMatch == Search.Match.FIELD) {
                    map = new HashMap<>(fields.size());

                    for (String field : fields) {
                        String fieldValue = document.get(field);
                        map.put(field, fieldValue);
                    }
                } else {
                    List<IndexableField> documentFields = document.getFields();
                    map = new HashMap<>(documentFields.size());
                    for (IndexableField indexableField : documentFields) {
                        map.put(indexableField.name(), indexableField.stringValue());
                    }
                }
                data.add(map);
            }
        });
        documentData.accomplish();
        return documentData;
    }

    @Override
    public <T> DocumentData<T> search(Search search, Class<T> mapClass) throws Exception {
        DocumentData<Map<String, Object>> documentData = search(search);
        if(null == documentData) {
            return null;
        }
        DocumentData<T> documentData1 = new DocumentData<>();

        documentData1.setData(Collections.emptyList());
        documentData1.setHit(documentData.getHit());

        if (documentData.isEmpty()) {
            return documentData1;
        }

        List<Map<String, Object>> documentDataData = documentData.getData();
        T object = ClassHelper.forObject(mapClass);
        if (null == object) {
            return documentData1;
        }
        List<T> newData = new ArrayList<>(documentDataData.size());
        for (Map<String, Object> dataDatum : documentDataData) {
            T object1 = ClassHelper.forObject(mapClass);

            BeansHelper.reflectionAssignment(object1, dataDatum);
            newData.add(object1);
        }
        documentData1.setData(newData);
        return documentData1;
    }

    @Override
    public void deleteAll() throws Exception {
        indexWriter.deleteAll();
    }

    @Override
    public void close() throws Exception {
        indexWriter.close();
        directory.close();
    }

}

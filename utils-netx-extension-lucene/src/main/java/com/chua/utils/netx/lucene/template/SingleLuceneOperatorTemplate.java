package com.chua.utils.netx.lucene.template;

import com.chua.utils.netx.entity.DocumentMap;
import com.chua.utils.netx.entity.Search;
import com.chua.utils.netx.function.DocumentContextAware;
import com.chua.utils.netx.lucene.aware.MMapLuceneContextAware;
import com.chua.utils.netx.lucene.aware.NioFSLuceneContextAware;
import com.chua.utils.tools.template.template.LuceneOperatorTemplate;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * lucene操作模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
public class SingleLuceneOperatorTemplate extends FileLuceneOperatorTemplate {

    private String tableName;
    private DocumentContextAware nioFSLuceneContextAware;

    public SingleLuceneOperatorTemplate(String tableName) throws IOException {
        this.tableName = tableName;
        this.nioFSLuceneContextAware = new NioFSLuceneContextAware(Paths.get(database, tableName));
    }

    @Override
    public void createTable(String name) throws Exception {
    }

    @Override
    public boolean isExist(String name) {
        return super.isExist(name);
    }

    @Override
    public void updateDocument(String name, DocumentMap doc) throws Exception {
        nioFSLuceneContextAware.updateDocument(doc);
    }

    @Override
    public void addDocument(String name, DocumentMap doc) throws Exception {
        nioFSLuceneContextAware.addDocument(doc);
    }

    @Override
    public long deleteDocument(String name, String expression) throws Exception {
        return nioFSLuceneContextAware.deleteDocument(expression);
    }

    @Override
    public void addDocuments(String name, List<DocumentMap> docs) throws Exception {
        nioFSLuceneContextAware.addDocuments(docs);
    }

    @Override
    public List<Map<String, Object>> queryForList(String name, Search search) throws Exception {
        return nioFSLuceneContextAware.search(search).getData();
    }

    @Override
    public List<Map<String, Object>> keyword(String name, Search search) throws Exception {
        return nioFSLuceneContextAware.searchKeyword(search).getData();
    }

    @Override
    public <Entity> List<Entity> queryForList(String name, Search search, Class<Entity> tClass) throws Exception {
        return nioFSLuceneContextAware.search(search, tClass).getData();
    }

    @Override
    protected DocumentContextAware getDocumentContextAware(String name) throws IOException {
        return nioFSLuceneContextAware;
    }
}


package com.chua.utils.netx.lucene.template;

import com.chua.utils.netx.entity.DocumentData;
import com.chua.utils.netx.entity.DocumentMap;
import com.chua.utils.netx.entity.Search;
import com.chua.utils.netx.function.DocumentContextAware;
import com.chua.utils.netx.lucene.aware.NioFSLuceneContextAware;
import com.chua.utils.tools.template.template.LuceneOperatorTemplate;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
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
@Slf4j
public class FileLuceneOperatorTemplate implements LuceneOperatorTemplate<DocumentMap, Search> {

    protected String database = System.getProperty("user.home") + "/data";

    @Override
    public void createTable(String name) throws Exception {
        if (isExist(name)) {
            log.info("[" + name + "] existed");
        }
        try (DocumentContextAware documentContextAware = getDocumentContextAware(name)) {
            return;
        }
    }

    @Override
    public boolean isExist(String name) {
        Path path = Paths.get(database, name);
        return path.toFile().exists();
    }

    @Override
    public void updateDocument(String name, DocumentMap doc) throws Exception {
        if (!isExist(name)) {
            throw new IllegalStateException("Table [" + name + "] does not exist ");
        }
        try (DocumentContextAware documentContextAware = getDocumentContextAware(name)) {
            documentContextAware.updateDocument(doc);
        }
    }

    @Override
    public void addDocument(String name, DocumentMap doc) throws Exception {
        if (!isExist(name)) {
            throw new IllegalStateException("Table [" + name + "] does not exist ");
        }
        try (DocumentContextAware documentContextAware = getDocumentContextAware(name)) {
            documentContextAware.addDocument(doc);
        }
    }

    @Override
    public long deleteDocument(String name, String expression) throws Exception {
        if (!isExist(name)) {
            throw new IllegalStateException("Table [" + name + "] does not exist ");
        }
        try (DocumentContextAware documentContextAware = getDocumentContextAware(name)) {
            return documentContextAware.deleteDocument(expression);
        }
    }


    @Override
    public void addDocuments(String name, List<DocumentMap> docs) throws Exception {
        if (!isExist(name)) {
            throw new IllegalStateException("Table [" + name + "] does not exist ");
        }
        try (DocumentContextAware documentContextAware = getDocumentContextAware(name)) {
            documentContextAware.addDocuments(docs);
            return;
        }
    }

    @Override
    public List<Map<String, Object>> queryForList(String name, Search search) throws Exception {
        if (!isExist(name)) {
            throw new IllegalStateException("Table [" + name + "] does not exist ");
        }
        try (DocumentContextAware documentContextAware = getDocumentContextAware(name)) {
            DocumentData<Map<String, Object>> documentData = documentContextAware.search(search);
            return documentData.getData();
        }

    }

    @Override
    public List<Map<String, Object>> keyword(String name, Search search) throws Exception {
        if (!isExist(name)) {
            throw new IllegalStateException("Table [" + name + "] does not exist ");
        }

        try (DocumentContextAware documentContextAware = getDocumentContextAware(name)) {
            DocumentData<Map<String, Object>> documentData = documentContextAware.searchKeyword(search);
            return documentData.getData();
        }
    }

    @Override
    public <Entity> List<Entity> queryForList(String name, Search search, Class<Entity> tClass) throws Exception {
        if (!isExist(name)) {
            throw new IllegalStateException("Table [" + name + "] does not exist ");
        }
        try (DocumentContextAware documentContextAware = getDocumentContextAware(name)) {
            DocumentData<Entity> documentData = documentContextAware.search(search, tClass);
            return documentData.getData();
        }
    }

    /**
     * 获取 DocumentContextAware
     *
     * @param name 名称
     * @return DocumentContextAware
     * @throws IOException IOException
     */
    protected DocumentContextAware getDocumentContextAware(String name) throws IOException {
        return new NioFSLuceneContextAware(Paths.get(database, name));
    }
}

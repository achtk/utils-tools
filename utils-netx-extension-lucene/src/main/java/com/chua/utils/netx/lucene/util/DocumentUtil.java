package com.chua.utils.netx.lucene.util;

import com.chua.utils.netx.lucene.entity.DataDocument;
import com.google.common.base.Strings;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * doc工具类
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/20 18:12
 */
public class DocumentUtil {

    private static final Pattern PATTERN = Pattern.compile("(and|or){1}");
    public static final String CREATE_TIME = "#createTime";
    public static final String ID = "id";
    public static ConcurrentMap<String, String> javaProperty2SqlColumnMap = new ConcurrentHashMap<>();

    static {
        javaProperty2SqlColumnMap.put("Integer", "INTEGER");
        javaProperty2SqlColumnMap.put("Short", "tinyint");
        javaProperty2SqlColumnMap.put("Long", "bigint");
        javaProperty2SqlColumnMap.put("BigDecimal", "decimal(19,2)");
        javaProperty2SqlColumnMap.put("Double", "double precision not null");
        javaProperty2SqlColumnMap.put("Float", "float");
        javaProperty2SqlColumnMap.put("Boolean", "bit");
        javaProperty2SqlColumnMap.put("Timestamp", "datetime");
        javaProperty2SqlColumnMap.put("String", "VARCHAR(255)");
    }

    /**
     * 对象转Doc
     *
     * @param article Map
     * @return List
     */
    public static List<Document> map2Documents(List<? extends Map<String, Object>> article) {
        List<Document> documents = new ArrayList<>(article.size());
        for (Map<String, Object> objectMap : article) {
            documents.add(map2Document(objectMap));
        }
        return documents;
    }

    /**
     * 对象转Doc
     *
     * @param article Map
     * @return Document
     */
    public static Document map2Document(Map<String, Object> article) {
        if (!article.containsKey(ID)) {
            throw new NullPointerException("Data needs to contain a unique ID");
        }
        Document document = new Document();

        for (Map.Entry<String, Object> entry : article.entrySet()) {
            TextField textField = new TextField(entry.getKey(), entry.getValue() + "", Store.YES);
            document.add(textField);
        }
        TextField createTimeField = new TextField(CREATE_TIME, System.currentTimeMillis() + "", Store.YES);
        document.add(createTimeField);

        return document;
    }

    /**
     * DataDocumenDataDocument 转 document
     *
     * @param dataDocument
     * @return
     */
    public static Iterable<? extends IndexableField> map2Document(DataDocument dataDocument) {
        if (Strings.isNullOrEmpty(dataDocument.getDataId())) {
            throw new NullPointerException("Data needs to contain a unique dataId");
        }
        Document document = new Document();

        for (Map.Entry<String, Object> entry : dataDocument.getData().entrySet()) {
            Object value = entry.getValue();
            Field textField = null;
            if (value instanceof String) {
                textField = new TextField(entry.getKey(), entry.getValue() + "", Store.YES);
            } else if (value instanceof Integer) {
                textField = new StoredField(entry.getKey(), (Integer) entry.getValue());
            } else if (value instanceof Long) {
                textField = new StoredField(entry.getKey(), (Long) entry.getValue());
            } else if (value instanceof Float) {
                textField = new StoredField(entry.getKey(), (Float) entry.getValue());
            } else if (value instanceof Double) {
                textField = new StoredField(entry.getKey(), (Double) entry.getValue());
            } else if (value instanceof byte[]) {
                textField = new StoredField(entry.getKey(), (byte[]) entry.getValue());
            }
            document.add(textField);
        }
        TextField createTimeField = new TextField(CREATE_TIME, System.currentTimeMillis() + "", Store.YES);
        document.add(createTimeField);
        TextField idField = new TextField("#" + ID, dataDocument.getDataId(), Store.YES);
        document.add(idField);

        return document;
    }
}
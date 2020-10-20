package com.chua.utils.netx.lucene.util;

import com.chua.utils.netx.lucene.entity.Article;
import com.google.common.base.Splitter;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexableField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * doc工具类
 * @author CH
 * @version 1.0
 * @since 2020/10/20 18:12
 */
public class DocumentUtil {

    private static final Pattern PATTERN = Pattern.compile("(and|or){1}");
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
     * @param article
     * @return
     */
    public static Document article2Document(Article article) {
        Document document = new Document();
        FieldType fieldType = new FieldType();
        fieldType.setStored(article.isStored());

        Field field = new Field(article.getName(), String.valueOf(article.getValue()), fieldType);

        document.add(field);
        return document;
    }
    /**
     * 对象转Doc
     * @param article
     * @return
     */
    public static Document article2Document(Map<String, Object> article) {
        Document document = new Document();
        FieldType fieldType = new FieldType();

        for (Map.Entry<String, Object> entry : article.entrySet()) {
            Field field = new Field(entry.getKey(), String.valueOf(entry.getValue()), fieldType);

            document.add(field);
        }
        return document;
    }

    /**
     * 获取结果
     * @param fields
     * @return
     */
    public static List<Map<String, Object>> toList(List<IndexableField> fields) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (IndexableField indexableField : fields) {
            Map<String, Object> item = new HashMap<>();
            item.put(indexableField.name(), indexableField.stringValue());

            result.add(item);
        }

        return result;
    }

    /**
     * 获取字段
     * @param keyWord
     * @return
     */
    public static String[] toFields(String keyWord) {
        List<String> strings = Splitter.on(PATTERN).trimResults().omitEmptyStrings().splitToList(keyWord);
        List<String> fields = new ArrayList<>();
        for (String string : strings) {
            List<String> stringList = Splitter.on(":").splitToList(string);
            fields.add(stringList.get(0));
        }
        return fields.toArray(new String[0]);
    }
}
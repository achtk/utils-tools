package com.chua.utils.netx.flink.table;

import com.chua.utils.netx.flink.format.FormatConnector;
import com.chua.utils.tools.table.Table;
import com.chua.utils.tools.util.ClassUtils;
import com.chua.utils.tools.util.CollectionUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mem
 *
 * @author CH* @since 2021/1/26
 * @version 1.0.0
 */
public class MemTable implements Table {

    private Map<String, Object> columns = new HashMap<>();
    private String tableName;
    private List<Object> data;

    @Override
    public String create() {
        String column = Joiner.on(",\n").withKeyValueSeparator(" ").join(columns);
        if (Strings.isNullOrEmpty(column)) {
            column = byType(CollectionUtils.findFirst(data));
        }
        String template = "CREATE TABLE %s (%s) WITH (\"connector.type\" = \"mem\",\"schema.table\" = \"%s\")";
        return String.format(template, tableName, column, tableName);
    }

    @Override
    public void notice() {
        FormatConnector.setPropertiesByTable(tableName, data);
    }

    /**
     * 数据获取字段
     *
     * @param obj 对象
     * @return
     */
    String byType(Object obj) {
        Map<String, String> result = new HashMap<>();
        Map<String, Object> transform = new HashMap<>();
        if (!(obj instanceof Map)) {
            transform = BeanMap.create(obj);
        }
        transform.forEach((k, v) -> {
            result.put("`" + k + "`", null == v ? "STRING" : ClassUtils.java2JdbcType(v));
        });
        return Joiner.on(",\n").withKeyValueSeparator(" ").join(result);
    }

    static class Builder implements Table.Builder<MemTable, List<Object>> {

        private MemTable memTable = new MemTable();

        @Override
        public Table.Builder table(String name) {
            memTable.tableName = name;
            return this;
        }

        @Override
        public Table.Builder source(List<Object> data) {
            memTable.data = data;
            return this;
        }

        @Override
        public Table.Builder column(String columnName, String columnType) {
            memTable.columns.put(columnName, columnType);
            return this;
        }

        @Override
        public MemTable build() {
            return memTable;
        }
    }
}

package com.chua.utils.netx.flink.table;

import com.chua.utils.tools.table.Table;
import com.google.common.base.Joiner;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件表
 *
 * @author CH* @since 2021/1/25
 * @version 1.0.0
 */
public class FileTable implements Table {
    private String tableName;
    private Map<String, Object> columns = new HashMap<>();
    private String file;
    private String type = "csv";

    @Override
    public String create() {
        String column = Joiner.on(",\r\n").withKeyValueSeparator(" ").join(columns);
        String template = "CREATE TABLE %s (%s) WITH (\"connector.type\" = \"filesystem\",\"connector.path\" = \"%s\", \"format.type\" = \"%s\")";
        return String.format(template, tableName, column, file, type);
    }

    /**
     * 构造器
     */
    static class Builder implements Table.Builder<FileTable, String> {

        private FileTable fileTable = new FileTable();

        @Override
        public Table.Builder source(String path) {
            fileTable.file = path;
            return this;
        }

        public Table.Builder type(String type) {
            fileTable.type = type;
            return this;
        }

        @Override
        public Table.Builder table(String name) {
            fileTable.tableName = name;
            return this;
        }

        @Override
        public Table.Builder column(String columnName, String columnType) {
            fileTable.columns.put(columnName, columnType);
            return this;
        }

        @Override
        public FileTable build() {
            return fileTable;
        }
    }
}

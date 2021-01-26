package com.chua.utils.netx.flink.table

import com.google.common.base.Joiner

/**
 * 文件表
 * @author CH* @since 2021/1/25
 * @version 1.0.0
 */
class FileTable implements Table {
    String tableName
    def columns = [:] as Map<String, Object>
    String file
    String type = 'csv'

    @Override
    String create() {
        def column = Joiner.on(",\r\n").withKeyValueSeparator(" ").join(columns)
        """
            CREATE TABLE ${tableName} (
              ${column}
            ) WITH (
              'connector.type' = 'filesystem',
              'connector.path' = '${file}',
              'format.type' = '${type}'
              )
        """
    }
    /**
     * 构造器
     */
    static class Builder implements Table.Builder<FileTable, String> {

        private FileTable fileTable = new FileTable();

        @Override
        Table.Builder source(String path) {
            fileTable.file = path
            this
        }

        Table.Builder type(String type = 'csv') {
            fileTable.type = type
            this
        }

        @Override
        Table.Builder table(String name) {
            fileTable.tableName = name
            this
        }

        @Override
        Table.Builder column(String columnName, String columnType) {
            fileTable.columns[columnName] = columnType
            this
        }

        @Override
        FileTable build() {
            fileTable
        }
    }
}

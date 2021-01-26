package com.chua.utils.netx.flink.table

import com.chua.utils.netx.flink.format.FormatConnector
import com.chua.utils.tools.util.ClassUtils
import com.chua.utils.tools.util.CollectionUtils
import com.google.common.base.Joiner
import com.google.common.base.Strings
import net.sf.cglib.beans.BeanMap

/**
 * mem
 * @author CH* @since 2021/1/26
 * @version 1.0.0
 */
class MemTable implements Table {

    def columns = [:] as Map<String, Object>
    String tableName
    List<?> data

    @Override
    String create() {
        def column = Joiner.on(",\n").withKeyValueSeparator(" ").join(columns)
        if (Strings.isNullOrEmpty(column)) {
            column = byType(CollectionUtils.findFirst(data));
        }
        """
        CREATE TABLE ${tableName} (
          ${column}
        ) WITH (
          'connector.type' = 'mem',
          'schema.table' = '${tableName}'
        )
      """
    }

    @Override
    void notice() {
        FormatConnector.setPropertiesByTable(tableName, data)
    }
    /**
     * 数据获取字段
     * @param obj 对象
     * @return
     */
    String byType(Object obj) {
        def result = [:] as Map<String, String>
        def transform = [:] as Map<String, Object>
        if (obj !instanceof Map) {
            transform = BeanMap.create(obj);
        }
        transform.each {
            k, v -> {
                result['`' + k + '`'] = null == v ? "STRING" : ClassUtils.java2JdbcType(v)
            }
        }
        Joiner.on(",\n").withKeyValueSeparator(" ").join(result)
    }

    static class Builder implements Table.Builder<MemTable, List<?>> {

        private MemTable memTable = new MemTable()

        @Override
        Table.Builder table(String name) {
            memTable.tableName = name
            this
        }

        @Override
        Table.Builder source(List<?> data) {
            memTable.data = data
            this
        }

        @Override
        Table.Builder column(String columnName, String columnType) {
            memTable.columns[columnName] = columnType
            this
        }

        @Override
        MemTable build() {
            memTable
        }
    }
}

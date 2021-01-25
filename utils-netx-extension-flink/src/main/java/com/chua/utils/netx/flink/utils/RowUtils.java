package com.chua.utils.netx.flink.utils;

import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.function.converter.TypeConverter;
import org.apache.flink.table.api.TableColumn;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * row工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
public class RowUtils {
    /**
     * row 2 map
     *
     * @param record 记录
     * @param schema schma
     * @return Map
     */
    public static Map<String, Object> toMap(Row record, TableSchema schema) {
        if (null == schema || null == record) {
            return Collections.emptyMap();
        }

        List<TableColumn> tableColumns = schema.getTableColumns();
        int index = 0;
        int arity = record.getArity();
        Map<String, Object> result = new HashMap<>();
        for (TableColumn tableColumn : tableColumns) {
            Class<?> aClass = tableColumn.getType().getConversionClass();
            TypeConverter converter = EmptyOrBase.getTypeConverter(aClass);
            if (arity >= index) {
                result.put(tableColumn.getName(), converter.convert(record.getField(index++)));
            }
        }
        return result;
    }
}

package com.chua.utils.netx.flink.format;

import com.chua.utils.netx.flink.utils.RowUtils;
import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.util.ClassUtils;
import org.apache.flink.table.api.TableColumn;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mem Output
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/26
 */
public class MemOutputFormat extends FlinkOutputFormat {

    @Override
    public void open(int taskNumber, int numTasks) throws IOException {

    }

    @Override
    public void writeRecord(Row record) throws IOException {
        Class<?> dataType = FormatConnector.getDataType(sign);
        if (null == dataType) {
            try {
                throw new Exception("Unrecognized data");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Map.class.isAssignableFrom(dataType)) {
            intoMap(record);
            return;
        }

        Object forObject = ClassUtils.forObject(dataType);
        if (null == forObject) {
            try {
                throw new Exception("No valid null parameter construction");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        intoEntity(record, forObject);
    }

    /**
     * 保存记录
     *
     * @param record    记录
     * @param forObject 对象
     */
    private void intoEntity(Row record, Object forObject) {
        Object item = BeanCopy.of(forObject).with(RowUtils.toMap(record, FormatConnector.getSchema(sign))).create();
        FormatConnector.addData(sign, item);
    }

    /**
     * 设置map值
     *
     * @param record 记录
     */
    private void intoMap(Row record) {
        TableSchema schema = FormatConnector.getSchema(sign);
        int arity = record.getArity();
        List<TableColumn> tableColumns = schema.getTableColumns();
        Map<String, Object> item = new HashMap<>();
        if (tableColumns.size() >= arity) {
            for (int i = 0; i < arity; i++) {
                TableColumn tableColumn = schema.getTableColumn(i).get();
                item.put(tableColumn.getName(), EmptyOrBase.getTypeConverter(tableColumn.getType().getConversionClass()).convert(record.getField(i)));
            }
        } else {
            for (int i = 0; i < tableColumns.size(); i++) {
                TableColumn tableColumn = tableColumns.get(i);
                item.put(tableColumn.getName(), EmptyOrBase.getTypeConverter(tableColumn.getType().getConversionClass()).convert(record.getField(i)));
            }
        }
        FormatConnector.addData(sign, item);
    }

    @Override
    public void close() throws IOException {

    }
}

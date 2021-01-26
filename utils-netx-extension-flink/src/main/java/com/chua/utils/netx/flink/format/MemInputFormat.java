package com.chua.utils.netx.flink.format;

import com.chua.utils.tools.empty.EmptyOrBase;
import net.sf.cglib.beans.BeanMap;
import org.apache.flink.table.api.TableColumn;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * mem input
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/26
 */
public class MemInputFormat extends FlinkInputFormat {

    private boolean hasNext;
    private List<Map<String, Object>> list;
    private AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void openInputFormat() throws IOException {
        this.list = toMap(FormatConnector.getData(sign, List.class));
        this.hasNext = null != list && !list.isEmpty();
        super.openInputFormat();
    }


    @Override
    public boolean reachedEnd() throws IOException {
        return !hasNext;
    }

    @Override
    public Row nextRecord(Row reuse) throws IOException {
        int size = list.size();
        atomicInteger.incrementAndGet();
        if(atomicInteger.get() >= size) {
            hasNext = false;
        }
        Map<String, Object> stringObjectMap = list.get(atomicInteger.get() - 1);
        TableSchema schema = FormatConnector.getSchema(sign);
        List<TableColumn> tableColumns = schema.getTableColumns();
        int index = 0;
        for (TableColumn tableColumn : tableColumns) {
            Object o = stringObjectMap.get(tableColumn.getName());
            reuse.setField(index ++ , EmptyOrBase.getTypeConverter(tableColumn.getType().getConversionClass()).convert(o));
        }
        return reuse;
    }

    @Override
    public void close() throws IOException {
        this.list = toMap(FormatConnector.getData(sign, List.class));
        this.atomicInteger.set(0);
    }

    /**
     * entity -> map
     *
     * @param data data
     * @return List<Map>
     */
    private List<Map<String, Object>> toMap(List<Object> data) {
        if (null == data || data.isEmpty()) {
            return Collections.emptyList();
        }
        return data.stream().map(item -> {
            if (item instanceof Map) {
                return (Map<String, Object>) item;
            }
            return (Map<String, Object>) BeanMap.create(item);
        }).collect(Collectors.toList());
    }
}

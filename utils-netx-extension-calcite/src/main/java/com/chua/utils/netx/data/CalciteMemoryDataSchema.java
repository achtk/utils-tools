package com.chua.utils.netx.data;

import com.chua.utils.netx.data.calcite.MemoryDataSchema;
import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.data.ShareMemDataSchema;
import com.chua.utils.tools.data.table.DataTable;
import com.google.common.collect.Maps;
import lombok.NoArgsConstructor;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
@NoArgsConstructor
public class CalciteMemoryDataSchema implements ShareMemDataSchema, SchemaFactory {

    private static final String MEMORY_DATA = "memory-data";
    private static final String MEMORY_TYPE = "memory-type";
    private String schema = "system";
    private Map<String, List<?>> data = Maps.newHashMap();

    public CalciteMemoryDataSchema(Map<String, List<?>> data) {
        this.data = data;
    }

    public CalciteMemoryDataSchema(List<?> data) {
        this.data.put("TEMP", data);
    }

    @Override
    public Schema create(SchemaPlus schemaPlus, String s, Map<String, Object> map) {
        data = (Map<String, List<?>>) MapOperableHelper.getMap(map, MEMORY_DATA);
        if (null == data) {
            data = Collections.emptyMap();
        }
        return new MemoryDataSchema(data);
    }


    @Override
    public void initial(Object s) {

    }

    @Override
    public DataTable getTable() {
        return null;
    }

    @Override
    public String schema() {
        return schema;
    }

    @Override
    public void schema(String schema) {
        this.schema = schema;
    }

    @Override
    public HashOperateMap operand() {
        HashOperateMap hashOperateMap = HashOperateMap.create(MEMORY_DATA, data);
        if (null != data) {
            Map<String, Class<?>> classMap = new HashMap<>();
            data.forEach((key, value) -> {
                classMap.put(key, null == value || value.size() == 0 ? null : value.get(0).getClass());
            });
            hashOperateMap.put(MEMORY_TYPE, classMap);
        }
        return hashOperateMap;
    }

    @Override
    public void doWith(Consumer<Stream> consumer) {
        if (null == consumer) {
            return;
        }
        consumer.accept(data.entrySet().stream());
    }
}

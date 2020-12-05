package com.chua.utils.netx.data;

import com.chua.utils.netx.data.calcite.MemoryDataSchema;
import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.data.table.DataTable;
import com.google.common.collect.Maps;
import lombok.NoArgsConstructor;
import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
@NoArgsConstructor
public class CalciteShareDataSchema extends CalciteMemoryDataSchema {

    private String schema = "system";
    private static final Map<String, List<?>> SHARE_DATA = Maps.newHashMap();

    public CalciteShareDataSchema(Map<String, List<?>> data) {
        SHARE_DATA.putAll(data);
    }

    public CalciteShareDataSchema(List<?> data) {
        SHARE_DATA.put("TEMP", data);
    }

    @Override
    public Schema create(SchemaPlus schemaPlus, String s, Map<String, Object> map) {
        return new MemoryDataSchema(SHARE_DATA);
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
        return HashOperateMap.create();
    }
}

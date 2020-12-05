package com.chua.utils.tools.example.factory;

import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
public class CsvSchemaFactory implements SchemaFactory {
    @Override
    public Schema create(SchemaPlus schemaPlus, String s, Map<String, Object> map) {
        return new CsvSchema(map.get("directory").toString());
    }
}

package com.chua.utils.netx.data.schema;

import com.chua.utils.tools.collects.map.MapOperableHelper;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.util.List;
import java.util.Map;

/**
 * 方解石数据架图工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
public class CalciteDataSchemaFactory implements SchemaFactory {

    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        List<String> ids = MapOperableHelper.getList(operand, "id", String.class);
        return new CalciteDataSchema(ids);
    }
}

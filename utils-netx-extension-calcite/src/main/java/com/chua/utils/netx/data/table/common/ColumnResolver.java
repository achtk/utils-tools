package com.chua.utils.netx.data.table.common;

import com.google.common.collect.Lists;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Pair;

import java.util.List;
import java.util.Map;

/**
 * 字段解析器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/7
 */
public interface ColumnResolver {
    /**
     * 解析字段类型
     *
     * @param typeFactory  类型工厂
     * @param tableHeaders 表头
     * @return 数据类型
     */
    default RelDataType converterRowType(RelDataTypeFactory typeFactory, Map<String, String> tableHeaders) {
        List<String> names = Lists.newLinkedList();
        List<RelDataType> types = Lists.newLinkedList();

        tableHeaders.forEach((name, type) -> {
            names.add(name);
            types.add(typeFactory.createSqlType(SqlTypeName.get(type)));
        });
        return typeFactory.createStructType(Pair.zip(names, types));
    }
}

package com.chua.utils.netx.data.calcite;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.enums.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Pair;

import java.util.*;
import java.util.function.Consumer;

/**
 * 内存数据
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/5
 */
@AllArgsConstructor
public class MemoryDataSchema extends AbstractSchema {

    private Map<String, List<?>> data;

    @Override
    protected Map<String, Table> getTableMap() {
        ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
        data.forEach((table, value) -> {
            builder.put(table, new CalciteTable(value));
        });
        return builder.build();
    }

    /**
     * 内存表
     */
    @AllArgsConstructor
    private class CalciteTable extends AbstractTable implements ScannableTable {

        private final List<?> value;

        @Override
        public Enumerable<Object[]> scan(DataContext root) {
            return new AbstractEnumerable<Object[]>() {
                @Override
                public Enumerator<Object[]> enumerator() {
                    return new Enumerator<Object[]>() {

                        private Iterator<?> iterator;
                        private Object[] current;

                        {
                            this.reset();
                        }

                        @Override
                        public Object[] current() {
                            return current;
                        }

                        @Override
                        public boolean moveNext() {
                            if (iterator.hasNext()) {
                                Object next = iterator.next();
                                if (next instanceof Map) {
                                    Collection values = ((Map) next).values();
                                    this.current = ArraysHelper.toArray(values);
                                } else {
                                    List<Object> values = new ArrayList<>();
                                    ClassHelper.doWithFields(next.getClass(), field -> {
                                        values.add(ClassHelper.getFieldValue(next, field));
                                    });
                                    this.current = ArraysHelper.toArray(values);
                                }
                                return true;
                            }
                            return false;
                        }

                        @Override
                        public void reset() {
                            this.iterator = value.iterator();
                        }

                        @Override
                        public void close() {

                        }
                    };
                }
            };
        }

        @Override
        public RelDataType getRowType(RelDataTypeFactory relDataTypeFactory) {
            JavaTypeFactory typeFactory = (JavaTypeFactory) relDataTypeFactory;
            if (null == value) {
                return typeFactory.createStructType(Pair.zip(Collections.emptyList(), Collections.emptyList()));
            }
            List<String> columns = new ArrayList<>();
            List<RelDataType> columnType = new ArrayList<>();
            Object entity = value.get(0);
            if (!(entity instanceof Map)) {
                ClassHelper.doWithFields(entity.getClass(), field -> {
                    columns.add(field.getName().toUpperCase());
                    SqlTypeName name = SqlTypeName.get(field.getType().getSimpleName().toUpperCase());
                    if (null == name) {
                        name = SqlTypeName.ANY;
                    }
                    columnType.add(typeFactory.createSqlType(name));
                });
            } else {
                Map<String, Object> stringObjectMap = (Map<String, Object>) entity;
                stringObjectMap.forEach((key, value) -> {
                    if (!columns.contains(key)) {
                        columns.add(key.toUpperCase());
                        SqlTypeName name = null == value ? null : SqlTypeName.get(value.getClass().getSimpleName().toUpperCase());
                        if (null == name) {
                            name = SqlTypeName.ANY;
                        }
                        columnType.add(typeFactory.createSqlType(name));
                    }
                });
            }
            return typeFactory.createStructType(Pair.zip(columns, columnType));
        }
    }
}

package com.chua.utils.netx.flink.table;

import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.empty.EmptyOrBase;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;
import org.apache.flink.types.Row;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * tables
 *
 * @author CH* @version 1.0.0* @since 2021/1/25
 */
public class Tables {

    private ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();
    private BatchTableEnvironment tableEnvironment = BatchTableEnvironment.create(environment);

    /**
     * 实例
     *
     * @return Tables
     */
    public static Tables newInstance() {
        return new Tables();
    }

    /**
     * 注册表
     *
     * @param table 表
     * @return
     */
    public Tables register(Table table) {
        try {
            tableEnvironment.executeSql(table.create());
        } finally {
            table.notice();
        }
        return this;
    }

    /**
     * 数据表
     *
     * @return 数据表
     */
    public String[] listTables() {
        return tableEnvironment.listTables();
    }

    /**
     * 查询
     *
     * @return
     */
    public <T> List<T> sqlQuery(String sql, Class<T> tClass) throws Exception {
        org.apache.flink.table.api.Table newTable = tableEnvironment.sqlQuery(sql);
        DataSet dataSet = tableEnvironment.toDataSet(newTable, Row.class);
        List<Row> collect = dataSet.collect();
        if(Row.class.isAssignableFrom(tClass)) {
            return (List<T>) collect;
        }
        RowTypeInfo rowTypeInfo = (RowTypeInfo) dataSet.getType();
        String[] fieldNames = rowTypeInfo.getFieldNames();
        TypeInformation<?>[] fieldTypes = rowTypeInfo.getFieldTypes();
        return collect.stream().map(item -> {
            int arity = item.getArity();
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < arity; i++) {
                map.put(fieldNames[i], EmptyOrBase.getTypeConverter(fieldTypes[i].getTypeClass()).convert(item.getField(i)));
            }
            return BeanCopy.of(tClass).with(map).create();
        }).collect(Collectors.toList());
    }

    /**
     * 更新
     *
     * @return
     */
    public Integer sqlUpdate(String sql) throws Exception {
        TableResult tableResult = tableEnvironment.executeSql(sql);
        return tableResult.getJobClient().get().getJobStatus().get().ordinal();
    }

}

package com.chua.utils.tools.data.table.wrapper;

import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.data.table.DataTable;
import com.chua.utils.tools.data.table.type.TableType;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;

/**
 * 内存表修饰器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/12
 */
@AllArgsConstructor
public class DataSourceDataTableWrapper {

    private String name;

    private final TableType tableType = TableType.DATA_SOURCE;

    /**
     * 构建数据
     *
     * @param mapSource 驱动
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String mapSource) {
        return () -> {
            DataTable.DataTableBuilder builder = DataTable.builder();
            builder.tableType(tableType);
            builder.name(name);
            builder.operate(HashOperateMap.create(mapSource));
            return builder.build();
        };
    }

    /**
     * 构建数据
     *
     * @param mapSource 驱动(driver, url, username, password)
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String... mapSource) {
        int size = 3;
        if (mapSource.length < size) {
            return null;
        }
        return () -> {
            DataTable.DataTableBuilder builder = DataTable.builder();
            builder.tableType(tableType);
            builder.name(name);

            HashOperateMap operate = HashOperateMap.create();
            operate.put("jdbcDriver", mapSource[0]);
            operate.put("jdbcUrl", mapSource[1]);
            operate.put("jdbcUser", mapSource[2]);
            operate.put("jdbcPassword", mapSource[3]);
            builder.operate(operate);
            return builder.build();
        };
    }

    /**
     * 构建数据
     *
     * @param driver   驱动
     * @param url      url
     * @param username 用户名
     * @param password 密码
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String driver, String url, String username, String password) {
        return () -> {
            DataTable.DataTableBuilder builder = DataTable.builder();
            builder.tableType(tableType);
            builder.name(name);

            HashOperateMap operate = HashOperateMap.create();
            operate.put("jdbcDriver", driver);
            operate.put("jdbcUrl", url);
            operate.put("jdbcUser", username);
            operate.put("jdbcPassword", password);
            builder.operate(operate);
            return builder.build();
        };
    }

    /**
     * 构建数据
     *
     * @param dataSource 数据源
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(DataSource dataSource) {
        return () -> {
            DataTable.DataTableBuilder builder = DataTable.builder();
            builder.tableType(tableType);
            builder.source(dataSource);
            builder.name(name);
            return builder.build();
        };
    }

}

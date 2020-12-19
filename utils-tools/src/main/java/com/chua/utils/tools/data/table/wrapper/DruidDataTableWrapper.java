package com.chua.utils.tools.data.table.wrapper;

import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.data.table.DataTable;
import com.chua.utils.tools.data.table.type.TableType;

/**
 * druid
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/14
 */
public class DruidDataTableWrapper {

    private final TableType tableType = TableType.DRUID;
    /**
     * 构建数据
     *
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source() {
        return source("http://localhost");
    }
    /**
     * 构建数据
     *
     * @param host            druid地址
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(final String host) {
        return source(host + ":8082", host + ":8081");
    }
    /**
     * 构建数据
     *
     * @param url            url
     * @param coordinatorUrl coordinatorUrl
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(final String url, final String coordinatorUrl) {
        return () -> {
            DataTable.DataTableBuilder builder = DataTable.builder();
            builder.tableType(tableType);

            OperateHashMap operate = OperateHashMap.create();
            operate.put("url", url);
            operate.put("coordinatorUrl", coordinatorUrl);

            builder.operate(operate);
            return builder.build();
        };
    }

}

package com.chua.utils.tools.data.table.wrapper;

import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.data.table.DataTable;
import com.chua.utils.tools.data.table.type.TableType;

/**
 * es
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/14
 */
public class ElasticSearchDataTableWrapper {

    private final TableType tableType = TableType.ELASTIC_SEARCH;

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6379;
    private static final String INDEX = "default";
    private static final String PASSWORD = "";

    /**
     * 构建数据
     *
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source() {
        return source(HOST, PORT, INDEX, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host 主机
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String host) {
        return source(host, PORT, INDEX, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host 主机
     * @param port 端口
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String host, int port) {
        return source(host, port, INDEX, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host  主机
     * @param port  端口
     * @param index 索引
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String host, int port, String index) {
        return source(host, port, index, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host     主机
     * @param port     端口
     * @param index    索引
     * @param password 密码
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String host, int port, String index, String password) {
        return () -> {
            DataTable.DataTableBuilder builder = DataTable.builder();
            HashOperateMap.create(hashOperateMap -> {
                hashOperateMap.put("host", host);
                hashOperateMap.put("port", port);
                hashOperateMap.put("index", index);
                hashOperateMap.put("password", password);
                builder.operate(hashOperateMap);
            });
            builder.tableType(tableType);

            return builder.build();
        };
    }
}

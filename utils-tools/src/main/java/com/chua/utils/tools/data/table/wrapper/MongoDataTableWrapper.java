package com.chua.utils.tools.data.table.wrapper;

import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.data.table.DataTable;
import com.chua.utils.tools.data.table.type.TableType;

/**
 * mongo
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/14
 */
public class MongoDataTableWrapper {

    private final TableType tableType = TableType.MONGO;
    private static final String FACTORY = "org.apache.calcite.adapter.mongodb.MongoSchemaFactory";

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6379;
    private static final int DATABASE = 0;
    private static final String PASSWORD = "";

    /**
     * 构建数据
     *
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source() {
        return source(HOST, PORT, DATABASE, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host 主机
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String host) {
        return source(host, PORT, DATABASE, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host 主机
     * @param port 端口
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String host, int port) {
        return source(host, port, DATABASE, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host     主机
     * @param port     端口
     * @param database 密码
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String host, int port, int database) {
        return source(host, port, database, PASSWORD);
    }

    /**
     * 构建数据
     *
     * @param host     主机
     * @param port     端口
     * @param database 数据库
     * @param password 密码
     * @return DataTableWrapperBuilder
     */
    public DataTableWrapperBuilder source(String host, int port, int database, String password) {
        return () -> {
            DataTable.DataTableBuilder builder = DataTable.builder();
            OperateHashMap.create(hashOperateMap -> {
                hashOperateMap.put("host", host);
                hashOperateMap.put("port", port);
                hashOperateMap.put("database", database);
                hashOperateMap.put("password", password);
                builder.operate(hashOperateMap);
            });
            builder.tableType(tableType);

            return builder.build();
        };
    }
}

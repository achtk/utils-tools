package com.chua.utils.tools.data.factory;

import com.chua.utils.tools.data.table.DataTable;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/7
 */
public interface DataFactory {
    /**
     * 添加架图
     *
     * @param schema    架图名称
     * @param dataTable 数据表
     */
    void addSchema(String schema, DataTable dataTable);

    /**
     * 获取连接
     *
     * @return 连接
     * @throws SQLException SQLException
     */
    Connection getConnection() throws SQLException;

    /**
     * schema信息
     *
     * @return schema信息
     */
    String schema();

    /**
     * 获取url
     *
     * @return url
     */
    String getUrl();

    /**
     * 获取驱动
     *
     * @return 驱动
     */
    default String getDriver() {
        return "org.apache.calcite.jdbc.Driver";
    }
}

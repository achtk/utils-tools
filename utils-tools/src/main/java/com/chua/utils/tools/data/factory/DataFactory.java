package com.chua.utils.tools.data.factory;

import com.chua.utils.tools.data.table.DataTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.sql.*;
import java.util.List;
import java.util.Map;

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
     * 添加架图
     *
     * @param dataTable 数据表
     */
    default void addSchema(DataTable dataTable) {
        addSchema("system", dataTable);
    }

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

    /**
     * 创建数据源
     *
     * @param sql sql
     * @return 数据源
     * @throws Exception Exception
     */
    default List<Map<String, Object>> queryForList(final String sql) throws Exception {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return getData(resultSet);
    }

    /**
     * 格式化结果
     *
     * @param resultSet 结果
     * @return List
     * @throws Exception Exception
     */
    default List<Map<String, Object>> getData(ResultSet resultSet) throws Exception {
        List<Map<String, Object>> list = Lists.newArrayList();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnSize = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> map = Maps.newLinkedHashMap();
            for (int i = 1; i < columnSize + 1; i++) {
                map.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            list.add(map);
        }
        return list;
    }
}

package com.chua.utils.tools.template.template;

import com.chua.utils.tools.dsl.CreateTableStep;
import com.chua.utils.tools.dsl.CreateTableStepSqlBuilder;
import com.chua.utils.tools.dsl.SqlBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * jdbc操作模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public interface JdbcOperatorTemplate {

    /**
     * 操作
     *
     * @param express 表达式
     * @param params  请求参数
     * @return
     * @throws Exception
     */
    Object execute(final String express, final Object... params) throws Exception;

    /**
     * 更新操作
     *
     * @param express 表达式
     * @param params  请求参数
     * @return
     * @throws Exception
     */
    int update(final String express, final Object... params) throws Exception;

    /**
     * 插入操作
     *
     * @param express 表达式
     * @param data    数据
     * @return
     * @throws Exception
     */
    int[] batch(final String express, final List<List<Object>> data) throws Exception;

    /**
     * 插入操作
     *
     * @param express 表达式
     * @param data    数据
     * @return
     * @throws Exception
     */
    int[] batch(final String express, final Object[][] data) throws Exception;

    /**
     * 查询数据
     *
     * @param express 表达式
     * @param params  参数
     * @return List
     * @throws Exception
     */
    List<Map<String, Object>> queryForList(String express, final Object... params) throws Exception;

    /**
     * 查询数据
     *
     * @param express 表达式
     * @param params  参数
     * @param tClass  类型
     * @return List
     * @throws Exception
     */
    <T> List<T> queryForList(String express, Class<T> tClass, final Object... params) throws Exception;

    /**
     * 创建表
     *
     * @param createTableStepSqlBuilder 创建步骤
     * @throws Exception Exception
     */
    void createTable(CreateTableStepSqlBuilder createTableStepSqlBuilder) throws Exception;

    /**
     * 获取连接
     *
     * @return Connection
     * @throws SQLException SQLException
     */
    Connection getConnection() throws SQLException;
}

package com.chua.utils.tools.template.template;

import com.chua.utils.tools.dsl.CreateTableStepSqlBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
     * @param sql 表达式
     * @return Object
     * @throws Exception
     */
    int execute(final String sql) throws Exception;

    /**
     * 操作
     *
     * @param connection 链接
     * @param sql        表达式
     * @return Object
     * @throws Exception
     */
    int execute(final Connection connection, final String sql) throws Exception;

    /**
     * 操作
     *
     * @param connection 链接
     * @param autoClose  自动关闭链接
     * @param sql        表达式
     * @return Object
     * @throws Exception
     */
    int execute(final Connection connection, final boolean autoClose, final String sql) throws Exception;

    /**
     * 操作
     *
     * @param autoClose 自动关闭链接
     * @param sql       表达式
     * @param params    请求参数
     * @return Object
     * @throws Exception
     */
    Object executeCall(final boolean autoClose, final String sql, final Object... params) throws Exception;

    /**
     * 操作
     *
     * @param connection 链接
     * @param autoClose  自动关闭链接
     * @param sql        表达式
     * @param params     请求参数
     * @return Object
     * @throws Exception
     */
    Object executeCall(final Connection connection, final boolean autoClose, final String sql, final Object... params) throws Exception;

    /**
     * 更新操作
     *
     * @param connection 链接
     * @param sql        表达式
     * @param params     请求参数
     * @return
     * @throws Exception
     */
    int update(final Connection connection, final String sql, final Object... params) throws Exception;

    /**
     * 更新操作
     *
     * @param sql    表达式
     * @param params 请求参数
     * @return
     * @throws Exception
     */
    int update(final String sql, final Object... params) throws Exception;

    /**
     * 插入操作
     *
     * @param connection 链接
     * @param sql        表达式
     * @param params     数据
     * @return int[]
     * @throws Exception Exception
     */
    int[] batch(final Connection connection, final String sql, final Object[][] params) throws Exception;

    /**
     * 插入操作
     *
     * @param sql  表达式
     * @param data 数据
     * @return
     * @throws Exception
     */
    int[] batch(final String sql, final List<List<Object>> data) throws Exception;

    /**
     * 插入操作
     *
     * @param sql  表达式
     * @param data 数据
     * @return
     * @throws Exception
     */
    int[] batch(final String sql, final Object[][] data) throws Exception;

    /**
     * 查询数据
     *
     * @param sql    表达式
     * @param params 参数
     * @return List
     * @throws Exception
     */
    List<Map<String, Object>> queryForList(String sql, final Object... params) throws Exception;

    /**
     * 查询数据
     *
     * @param sql    表达式
     * @param params 参数
     * @param tClass 类型
     * @return List
     * @throws Exception
     */
    <T> List<T> queryForList(String sql, Class<T> tClass, final Object... params) throws Exception;

    /**
     * 查询数据
     *
     * @param connection 链接
     * @param sql        表达式
     * @param params     参数
     * @param tClass     类型
     * @return List
     * @throws Exception
     */
    <T> List<T> queryForList(Connection connection, String sql, Class<T> tClass, final Object... params) throws Exception;

    /**
     * 创建表
     *
     * @param createTableStepSqlBuilder 创建步骤
     * @throws Exception Exception
     */
    void createTable(CreateTableStepSqlBuilder createTableStepSqlBuilder) throws Exception;

    /**
     * 获取链接
     *
     * @return 链接
     * @throws SQLException SQLException
     */
    Connection getConnection() throws SQLException;

    /**
     * 获取驱动
     *
     * @return 驱动
     */
    String getDriver();

    /**
     * 获取链接
     *
     * @return 链接
     */
    String getUrl();
}

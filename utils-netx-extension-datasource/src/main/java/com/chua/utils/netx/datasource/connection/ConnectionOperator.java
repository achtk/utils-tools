package com.chua.utils.netx.datasource.connection;

import com.chua.utils.netx.datasource.config.StatementConfiguration;

import java.sql.*;
import java.util.Arrays;

/**
 * 链接操作器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/20
 */
public interface ConnectionOperator {
    /**
     * 获取链接
     *
     * @return 链接
     * @throws SQLException SQLException
     */
    Connection prepareConnection() throws SQLException;

    /**
     * 预处理参数
     *
     * @param statementConfiguration 预处理参数
     */
    void prepareStatementConfiguration(StatementConfiguration statementConfiguration);

    /**
     * 关闭连接
     *
     * @param connection 链接
     * @throws SQLException SQLException
     */
    void release(Connection connection) throws SQLException;

    /**
     * 关闭连接
     *
     * @param resultSet 链接
     * @throws SQLException SQLException
     */
    void release(ResultSet resultSet) throws SQLException;

    /**
     * 关闭连接
     *
     * @param statement 链接
     * @throws SQLException SQLException
     */
    void release(Statement statement) throws SQLException;

    /**
     * 预处理赋值
     *
     * @param stmt   stmt
     * @param params 参数
     * @throws SQLException SQLException
     */
    void fillStatement(PreparedStatement stmt, Object[] params) throws SQLException;

    /**
     * 预处理
     *
     * @param connection 链接
     * @param sql        语句
     * @return 预处理
     * @throws SQLException SQLException
     */
    PreparedStatement prepareStatement(Connection connection, String sql) throws SQLException;

    /**
     * 异常打印
     *
     * @param cause  异常
     * @param sql    sql
     * @param params 参数
     * @throws Exception Exception
     */
    default void rethrow(Exception cause, String sql, Object... params) throws Exception {

        String causeMessage = cause.getMessage();
        if (causeMessage == null) {
            causeMessage = "";
        }
        StringBuffer msg = new StringBuffer(causeMessage);

        msg.append(" Query: ");
        msg.append(sql);
        msg.append(" Parameters: ");

        if (params == null) {
            msg.append("[]");
        } else {
            msg.append(Arrays.deepToString(params));
        }

        Exception e = new Exception();
        if (cause instanceof SQLException) {
            SQLException throwables = new SQLException(msg.toString(), ((SQLException) cause).getSQLState(),
                    ((SQLException) cause).getErrorCode());
            throwables.setNextException((SQLException) cause);
            e = throwables;
        }
        throw e;
    }
}

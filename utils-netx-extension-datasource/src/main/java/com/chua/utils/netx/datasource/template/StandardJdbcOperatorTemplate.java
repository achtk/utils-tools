package com.chua.utils.netx.datasource.template;

import com.chua.utils.netx.datasource.connection.ConnectionOperator;
import com.chua.utils.netx.datasource.connection.JdbcConnectionOperator;
import com.chua.utils.netx.datasource.transform.JdbcOperatorTransform;
import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.bean.copy.StandardBeanCopy;
import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.dsl.CreateTableStepSqlBuilder;
import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.properties.OperatorProperties;
import com.chua.utils.tools.template.template.JdbcOperatorTemplate;
import com.chua.utils.tools.transform.OperatorTransform;
import com.google.common.base.CaseFormat;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 标准的jdbc工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/20
 */
public class StandardJdbcOperatorTemplate implements JdbcOperatorTemplate {

    private OperatorTransform<DataSource> operatorTransform = new JdbcOperatorTransform();
    private DataSource dataSource;
    private ConnectionOperator connectionOperator;
    private HashOperateMap operateMap;

    public StandardJdbcOperatorTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
        this.connectionOperator = new JdbcConnectionOperator(this.dataSource);
        this.operateMap = BeanCopy.of(dataSource).asMap();
    }

    public StandardJdbcOperatorTemplate(OperatorProperties operatorProperties) {
        this.dataSource = operatorTransform.transform(operatorProperties);
        this.connectionOperator = new JdbcConnectionOperator(this.dataSource);
        this.operateMap = BeanCopy.of(dataSource).asMap();
    }

    @Override
    public int execute(String sql) throws Exception {
        return execute(connectionOperator.prepareConnection(), sql);
    }

    @Override
    public int execute(Connection connection, String sql) throws Exception {
        return this.execute(connection, false, sql);
    }

    @Override
    public int execute(Connection connection, boolean autoClose, String sql) throws Exception {
        if (null == connection) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (autoClose) {
                connectionOperator.release(connection);
            }
            throw new SQLException("Null SQL statement");
        }

        Statement statement = null;
        int rows = 0;

        try {
            statement = connection.createStatement();
            statement.execute(sql);
            return statement.getUpdateCount();
        } catch (SQLException e) {
            connectionOperator.rethrow(e, sql, EmptyOrBase.EMPTY_STRING);
        } finally {
            connectionOperator.release(statement);
            if (autoClose) {
                connectionOperator.release(connection);
            }
        }
        return rows;
    }

    @Override
    public Object executeCall(boolean autoClose, String sql, Object... params) throws Exception {
        return executeCall(connectionOperator.prepareConnection(), autoClose, sql, params);
    }

    @Override
    public Object executeCall(Connection connection, boolean autoClose, String sql, Object... params) throws Exception {
        if (null == connection) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (autoClose) {
                connectionOperator.release(connection);
            }
            throw new SQLException("Null SQL statement");
        }

        CallableStatement stmt = null;
        int rows = 0;

        try {
            stmt = connection.prepareCall(sql);
            connectionOperator.fillStatement(stmt, params);
            stmt.execute();
            rows = stmt.getUpdateCount();
        } catch (SQLException e) {
            connectionOperator.rethrow(e, sql, params);
        } finally {
            connectionOperator.release(stmt);
            if (autoClose) {
                connectionOperator.release(connection);
            }
        }
        return rows;
    }

    @Override
    public int update(Connection connection, String sql, Object... params) throws Exception {
        if (connection == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            connectionOperator.release(connection);
            throw new SQLException("Null SQL statement");
        }

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = connectionOperator.prepareStatement(connection, sql);
            connectionOperator.fillStatement(stmt, params);
            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            connectionOperator.rethrow(e, sql, params);

        } finally {
            connectionOperator.release(stmt);
            connectionOperator.release(connection);
        }

        return rows;
    }

    @Override
    public int update(String sql, Object... params) throws Exception {
        return update(connectionOperator.prepareConnection(), sql, params);
    }

    @Override
    public int[] batch(Connection connection, String sql, Object[][] params) throws Exception {
        if (connection == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            connectionOperator.release(connection);
            throw new SQLException("Null SQL statement");
        }

        if (params == null) {
            connectionOperator.release(connection);
            throw new SQLException("Null parameters. If parameters aren't need, pass an empty array.");
        }

        PreparedStatement stmt = null;
        int[] rows = null;
        try {
            stmt = connectionOperator.prepareStatement(connection, sql);

            for (int i = 0; i < params.length; i++) {
                connectionOperator.fillStatement(stmt, params[i]);
                stmt.addBatch();
            }
            rows = stmt.executeBatch();

        } catch (SQLException e) {
            connectionOperator.rethrow(e, sql, (Object[]) params);
        } finally {
            connectionOperator.release(stmt);
            connectionOperator.release(connection);
        }

        return rows;
    }

    @Override
    public int[] batch(String sql, List<List<Object>> data) throws Exception {
        Object[][] objects = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            List<Object> objects1 = data.get(i);
            objects[i] = objects1.toArray(new Object[0]);
        }

        return batch(connectionOperator.prepareConnection(), sql, objects);
    }

    @Override
    public int[] batch(String sql, Object[][] data) throws Exception {
        return batch(connectionOperator.prepareConnection(), sql, data);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object... params) throws Exception {
        return queryForList(sql, EmptyOrBase.MAP_STRING_OBJECT, params);
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> tClass, Object... params) throws Exception {
        return queryForList(connectionOperator.prepareConnection(), sql, tClass, params);
    }

    @Override
    public <T> List<T> queryForList(Connection connection, String sql, Class<T> tClass, Object... params) throws Exception {
        if (connection == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            connectionOperator.release(connection);
            throw new SQLException("Null SQL statement");
        }

        if (tClass == null) {
            connectionOperator.release(connection);
            throw new SQLException("Null Type");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connectionOperator.prepareStatement(connection, sql);
            connectionOperator.fillStatement(stmt, params);
            rs = stmt.executeQuery();
            return this.handle(tClass, rs);

        } catch (Exception e) {
            connectionOperator.rethrow(e, sql, params);
        } finally {
            try {
                connectionOperator.release(rs);
            } finally {
                connectionOperator.release(stmt);
                connectionOperator.release(connection);
            }
        }

        return null;
    }

    /**
     * 处理对象
     *
     * @param tClass 类型
     * @param rs     结果
     * @param <T>    类型
     * @return 对象
     */
    private <T> List<T> handle(Class<T> tClass, ResultSet rs) throws Exception {
        List<T> result = new ArrayList<>();

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (rs.next()) {
            if(Map.class.isAssignableFrom(tClass)) {
                Map<String, Object> param = new HashMap<>();
                for (int i = 1; i < columnCount + 1; i++) {
                    param.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, metaData.getColumnName(i).toLowerCase()), rs.getObject(i));
                }
                result.add((T) param);
                continue;
            }
            BeanCopy<T> beanCopy = StandardBeanCopy.of(tClass);
            for (int i = 1; i < columnCount + 1; i++) {
                beanCopy.with(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, metaData.getColumnName(i).toLowerCase()), rs.getObject(i));
            }
            result.add(beanCopy.create());
        }
        return result;
    }

    @Override
    public void createTable(CreateTableStepSqlBuilder createTableStepSqlBuilder) throws Exception {

    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionOperator.prepareConnection();
    }

    @Override
    public String getDriver() {
        return operateMap.getString("driverClassName", "driver", "");
    }

    @Override
    public String getUrl() {
        return operateMap.getString("jdbcUrl", "url", "");
    }
}

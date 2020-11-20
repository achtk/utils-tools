package com.chua.utils.netx.datasource.connection;

import com.chua.utils.netx.datasource.config.StatementConfiguration;

import javax.sql.DataSource;
import java.sql.*;

/**
 * 链接操作器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/20
 */
public class JdbcConnectionOperator implements ConnectionOperator {

    private DataSource dataSource;

    /**
     * Is {@link ParameterMetaData#getParameterType(int)} broken (have we tried
     * it yet)?
     */
    private volatile boolean pmdKnownBroken = false;

    /**
     * Configuration to use when preparing statements.
     */
    private StatementConfiguration stmtConfig;

    public JdbcConnectionOperator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection prepareConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void prepareStatementConfiguration(StatementConfiguration statementConfiguration) {
        this.stmtConfig = statementConfiguration;
    }

    @Override
    public void release(Connection connection) throws SQLException {
        if (null != connection) {
            connection.close();
        }
    }

    @Override
    public void release(ResultSet resultSet) throws SQLException {
        if (null != resultSet) {
            resultSet.close();
        }
    }

    @Override
    public void release(Statement statement) throws SQLException {
        if (null != statement) {
            statement.close();
        }
    }

    @Override
    public void fillStatement(PreparedStatement stmt, Object[] params) throws SQLException {
        ParameterMetaData pmd = null;
        if (!pmdKnownBroken) {
            try {
                pmd = stmt.getParameterMetaData();
                if (pmd == null) {
                    pmdKnownBroken = true;
                } else {
                    int stmtCount = pmd.getParameterCount();
                    int paramsCount = params == null ? 0 : params.length;

                    if (stmtCount != paramsCount) {
                        throw new SQLException("Wrong number of parameters: expected "
                                + stmtCount + ", was given " + paramsCount);
                    }
                }
            } catch (SQLFeatureNotSupportedException ex) {
                pmdKnownBroken = true;
            }
        }

        if (params == null) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                stmt.setObject(i + 1, params[i]);
            } else {
                int sqlType = Types.VARCHAR;
                if (!pmdKnownBroken) {
                    try {
                        sqlType = pmd.getParameterType(i + 1);
                    } catch (SQLException e) {
                        pmdKnownBroken = true;
                    }
                }
                stmt.setNull(i + 1, sqlType);
            }
        }
    }

    @Override
    public PreparedStatement prepareStatement(Connection connection, String sql) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        try {
            configureStatement(ps);
        } catch (SQLException e) {
            ps.close();
            throw e;
        }
        return ps;
    }

    /**
     * 预处理
     *
     * @param stmt 预处理
     * @throws SQLException SQLException
     */
    private void configureStatement(Statement stmt) throws SQLException {
        if (stmtConfig == null) {
            return;
        }
        if (stmtConfig.isFetchDirectionSet()) {
            stmt.setFetchDirection(stmtConfig.getFetchDirection());
        }

        if (stmtConfig.isFetchSizeSet()) {
            stmt.setFetchSize(stmtConfig.getFetchSize());
        }

        if (stmtConfig.isMaxFieldSizeSet()) {
            stmt.setMaxFieldSize(stmtConfig.getMaxFieldSize());
        }

        if (stmtConfig.isMaxRowsSet()) {
            stmt.setMaxRows(stmtConfig.getMaxRows());
        }

        if (stmtConfig.isQueryTimeoutSet()) {
            stmt.setQueryTimeout(stmtConfig.getQueryTimeout());
        }
    }
}

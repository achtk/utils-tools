package com.chua.utils.netx.datasource.factory;

import com.chua.utils.netx.datasource.encrypt.Encrypt;
import com.chua.utils.netx.datasource.properties.DataSourceProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;

/**
 * 数据库工厂
 * @author CH
 */
public class DataSourceFactory {

    private DataSourceProperties dataSourceProperties;
    private Encrypt encrypt;
    private DataSource dataSource;

    public DataSourceFactory(DataSourceProperties dataSourceProperties, Encrypt encrypt) {
        this.dataSourceProperties = dataSourceProperties;
        this.encrypt = encrypt;
        this.dataSource = this.buildDataSource();
    }
    /**
     * 获取DataSource
     * @return
     */
    public DataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * 获取QueryRunner
     * @return
     */
    public QueryRunner getQueryRunner() {
        return new QueryRunner(dataSource);
    }
    /**
     * 获取QueryRunner
     * @return
     */
    public QueryRunner newQueryRunner() {
        return new QueryRunner();
    }
    /**
     * 构建DataSource
     * @return
     */
    private DataSource buildDataSource() {
        if(null == encrypt) {
            return new HikariDataSource(dataSourceProperties);
        }
        String password = dataSourceProperties.getPassword();
        dataSourceProperties.setPassword(encrypt.decode(password));
        return new HikariDataSource(dataSourceProperties);
    }

    /**
     * 构建
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * 构建
     */
    @Data
    @Accessors(fluent = true)
    @EqualsAndHashCode
    public static class Builder {
        private DataSourceProperties dataSourceProperties;
        private Encrypt encrypt;

        public DataSourceFactory build() {
            return new DataSourceFactory(dataSourceProperties, encrypt);
        }
    }
}

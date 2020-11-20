package com.chua.utils.netx.datasource.dialect;

import com.chua.utils.tools.common.StringHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库方言
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/21
 */
@Getter
@AllArgsConstructor
public enum SQLDialectEnum {
    /**
     * h2嵌入式
     */
    H2_EMBEDDED(SqlDialect.H2_DRIVER, SqlDialect.H2_EMBEDDED_URL, SqlDialect.H2),
    /**
     * h2内存
     */
    H2_MEMORY(SqlDialect.H2_DRIVER, SqlDialect.H2_MEMORY_URL, SqlDialect.H2),
    /**
     * h2服务式
     */
    H2_SERVER(SqlDialect.H2_DRIVER, SqlDialect.H2_SERVER_URL, SqlDialect.H2),
    /**
     * sqlite内存
     */
    SQLITE_MEMORY(SqlDialect.SQLITE_DRIVER, SqlDialect.SQLITE_MEMORY_URL, SqlDialect.SQLITE_MEMORY),
    /**
     * sqlite文件
     */
    SQLITE_FILE(SqlDialect.SQLITE_DRIVER, SqlDialect.SQLITE_FILE_URL, SqlDialect.SQLITE),
    /**
     * hsqldb 内存
     */
    HSQLDB_MEMORY(SqlDialect.HSQLDB_DRIVER, SqlDialect.HSQLDB_MEMORY_URL, SqlDialect.HSQLDB),
    /**
     * hsqldb 服务
     */
    HSQLDB_SERVER(SqlDialect.HSQLDB_DRIVER, SqlDialect.HSQLDB_SERVER_URL, SqlDialect.HSQLDB),
    /**
     * hsqldb 文件
     */
    HSQLDB_FILE(SqlDialect.HSQLDB_DRIVER, SqlDialect.HSQLDB_FILE_URL, SqlDialect.HSQLDB),
    /**
     * mysql
     */
    MYSQL(SqlDialect.MYSQL_DRIVER, SqlDialect.MYSQL_URL, SqlDialect.MYSQL),
    /**
     * oracle
     */
    ORACLE(SqlDialect.ORACLE_THIN_DRIVER, SqlDialect.ORACLE_THIN_URL, SqlDialect.ORACLE),
    /**
     * PostgreSQL
     */
    POSTGRESQL(SqlDialect.POSTGRE_SQL_DRIVER, SqlDialect.POSTGRE_SQL_URL, SqlDialect.POSTGRESQL),
    /**
     * JDBC-ODBC
     */
    JDBC_ODBC(SqlDialect.JDBC_ODBC_DRIVER, SqlDialect.JDBC_ODBC_URL, SqlDialect.JDBC_ODBC),
    /**
     * Informix
     */
    INFORMIX(SqlDialect.INFORMIX_DRIVER, SqlDialect.INFORMIX_URL, SqlDialect.INFORMIX),
    /**
     * db2
     */
    DB2(SqlDialect.DB2_DRIVER, SqlDialect.DB2_URL, SqlDialect.DB2),
    /**
     * db2-net
     */
    DB2_NET(SqlDialect.DB2_NET_DRIVER, SqlDialect.DB2_URL, SqlDialect.DB2),
    /**
     * sysbase
     */
    SYSBASE(SqlDialect.SYSBASE_DRIVER, SqlDialect.SYSBASE_URL, SqlDialect.SYSBASE),
    /**
     * sql_server_2_0
     */
    SQL_SERVER_2_0(SqlDialect.SQL_SERVER_DRIVER_2_0, SqlDialect.SQL_SERVER_URL_2_0, SqlDialect.SQL_SERVER_2_0),
    /**
     * sql_server_3_0
     */
    SQL_SERVER_3_0(SqlDialect.SQL_SERVER_DRIVER_3_0, SqlDialect.SQL_SERVER_URL_3_0, SqlDialect.SQL_SERVER_3_0),
    /**
     * mariadb
     */
    MARIADB(SqlDialect.MARIADB_URL, SqlDialect.MARIADB_DRIVER, SqlDialect.MARIADB);


    private String driver;
    private String url;
    private String dataSourceType;

    /**
     * 获取枚举类
     *
     * @param driver 驱动
     * @param url    地址
     * @return
     */
    public static SQLDialectEnum findByDriver(String driver, String url) {
        List<SQLDialectEnum> temp = new ArrayList<>();
        for (SQLDialectEnum value : SQLDialectEnum.values()) {
            if (value.getDriver().equals(driver)) {
                temp.add(value);
            }
        }
        if (temp.size() == 0) {
            return null;
        }
        if (temp.size() == 1) {
            return temp.get(0);
        }
        for (SQLDialectEnum sqlDialectEnum : temp) {
            String replace = sqlDialectEnum.getUrl().replace(SqlDialect.DB_NAME, "*").replace(SqlDialect.IP, "*").replace(SqlDialect.PORT, "*");
            if(StringHelper.wildcardMatch(url, replace)) {
                return sqlDialectEnum;
            }
        }
        return null;
    }
}

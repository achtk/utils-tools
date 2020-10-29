package com.chua.utils.netx.datasource.dialect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

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
    H2_EMBEDDED(SQLDialect.H2_DRIVER, SQLDialect.H2_EMBEDDED_URL, SQLDialect.H2),
    /**
     * h2内存
     */
    H2_MEMORY(SQLDialect.H2_DRIVER, SQLDialect.H2_MEMORY_URL, SQLDialect.H2),
    /**
     * h2服务式
     */
    H2_SERVER(SQLDialect.H2_DRIVER, SQLDialect.H2_SERVER_URL, SQLDialect.H2),
    /**
     * sqlite内存
     */
    SQLITE_MEMORY(SQLDialect.SQLITE_DRIVER, SQLDialect.SQLITE_MEMORY_URL, SQLDialect.SQLITE_MEMORY),
    /**
     * sqlite文件
     */
    SQLITE_FILE(SQLDialect.SQLITE_DRIVER, SQLDialect.SQLITE_FILE_URL, SQLDialect.SQLITE),
    /**
     * hsqldb 内存
     */
    HSQLDB_MEMORY(SQLDialect.HSQLDB_DRIVER, SQLDialect.HSQLDB_MEMORY_URL, SQLDialect.HSQLDB),
    /**
     * hsqldb 服务
     */
    HSQLDB_SERVER(SQLDialect.HSQLDB_DRIVER, SQLDialect.HSQLDB_SERVER_URL, SQLDialect.HSQLDB),
    /**
     * hsqldb 文件
     */
    HSQLDB_FILE(SQLDialect.HSQLDB_DRIVER, SQLDialect.HSQLDB_FILE_URL, SQLDialect.HSQLDB),
    /**
     * mysql
     */
    MYSQL(SQLDialect.MYSQL_DRIVER, SQLDialect.MYSQL_URL, SQLDialect.MYSQL),
    /**
     * oracle
     */
    ORACLE(SQLDialect.ORACLE_THIN_DRIVER, SQLDialect.ORACLE_THIN_URL, SQLDialect.ORACLE),
    /**
     * PostgreSQL
     */
    POSTGRESQL(SQLDialect.POSTGRE_SQL_DRIVER, SQLDialect.POSTGRE_SQL_URL, SQLDialect.POSTGRESQL),
    /**
     * JDBC-ODBC
     */
    JDBC_ODBC(SQLDialect.JDBC_ODBC_DRIVER, SQLDialect.JDBC_ODBC_URL, SQLDialect.JDBC_ODBC),
    /**
     * Informix
     */
    INFORMIX(SQLDialect.INFORMIX_DRIVER, SQLDialect.INFORMIX_URL, SQLDialect.INFORMIX),
    /**
     * db2
     */
    DB2(SQLDialect.DB2_DRIVER, SQLDialect.DB2_URL, SQLDialect.DB2),
    /**
     * db2-net
     */
    DB2_NET(SQLDialect.DB2_NET_DRIVER, SQLDialect.DB2_URL, SQLDialect.DB2),
    /**
     * sysbase
     */
    SYSBASE(SQLDialect.SYSBASE_DRIVER, SQLDialect.SYSBASE_URL, SQLDialect.SYSBASE),
    /**
     * sql_server_2_0
     */
    SQL_SERVER_2_0(SQLDialect.SQL_SERVER_DRIVER_2_0, SQLDialect.SQL_SERVER_URL_2_0, SQLDialect.SQL_SERVER_2_0),
    /**
     * sql_server_3_0
     */
    SQL_SERVER_3_0(SQLDialect.SQL_SERVER_DRIVER_3_0, SQLDialect.SQL_SERVER_URL_3_0, SQLDialect.SQL_SERVER_3_0),
    /**
     * mariadb
     */
    MARIADB(SQLDialect.MARIADB_URL, SQLDialect.MARIADB_DRIVER, SQLDialect.MARIADB);


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
            String replace = sqlDialectEnum.getUrl().replace(SQLDialect.DB_NAME, "*").replace(SQLDialect.IP, "*").replace(SQLDialect.PORT, "*");
            if(FilenameUtils.wildcardMatch(url, replace)) {
                return sqlDialectEnum;
            }
        }
        return null;
    }
}

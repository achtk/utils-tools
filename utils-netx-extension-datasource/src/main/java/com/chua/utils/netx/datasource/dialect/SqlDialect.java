package com.chua.utils.netx.datasource.dialect;

import net.sf.cglib.beans.BeanMap;

import javax.sql.DataSource;
import java.lang.reflect.Field;

/**
 * 数据库方言
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/21
 */
public class SqlDialect {

    public static String DB_NAME = "<db_name>";
    public static String PORT = "<port>";
    public static String IP = "<ip>";
    /**
     * MySQL 驱动程序
     */
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    /**
     * MySQL 链接地址
     * <p>jdbc:mysql://" + IP + ":" + PORT + "/DB_NAME</p>
     */
    public static final String MYSQL_URL = "jdbc:mysql://" + IP + ":" + PORT + "/" + DB_NAME;
    /**
     * Oracle(thin) 驱动程序
     */
    public static final String ORACLE_THIN_DRIVER = "oracle.jdbc.driver.OracleDriver";
    /**
     * Oracle 链接地址
     */
    public static final String ORACLE_THIN_URL = "jdbc:oracle:thin:@" + IP + ":" + PORT + ":" + DB_NAME;
    /**
     * PostgreSQL 驱动程序
     */
    public static final String POSTGRE_SQL_DRIVER = "org.postgresql.Driver";
    /**
     * PostgreSQL 链接地址
     */
    public static final String POSTGRE_SQL_URL = "jdbc:postgresql://" + IP + ":" + PORT + "/" + DB_NAME;
    /**
     * DB2 驱动程序
     * <p>连接具有DB2客户端的Provider实例</p>
     */
    public static final String DB2_DRIVER = "com.ibm.db2.jdbc.app.DB2.Driver";
    /**
     * DB2 驱动程序
     * <p>连接不具有DB2客户端的Provider实例</p>
     */
    public static final String DB2_NET_DRIVER = "com.ibm.db2.jdbc.net.DB2.Driver";
    /**
     * DB2 URL
     */
    public static final String DB2_URL = "jdbc:db2://" + IP + ":" + PORT + "/" + DB_NAME;
    /**
     * Sysbase 驱动程序
     */
    public static final String SYSBASE_DRIVER = "com.sybase.jdbc.SybDriver";
    /**
     * Sysbase URL
     */
    public static final String SYSBASE_URL = "jdbc:Sysbase://" + IP + ":" + PORT + "/" + DB_NAME;
    /**
     * Microsoft SQL Server 2.0 驱动程序
     */
    public static final String SQL_SERVER_DRIVER_2_0 = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
    /**
     * Microsoft SQL Server 2.0 URL
     */
    public static final String SQL_SERVER_URL_2_0 = "jdbc:microsoft:sqlserver://" + IP + ":" + PORT + ";DatabaseName=" + DB_NAME;
    /**
     * Microsoft SQL Server 3.0 驱动程序
     */
    public static final String SQL_SERVER_DRIVER_3_0 = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    /**
     * Microsoft SQL Server 3.0 URL
     */
    public static final String SQL_SERVER_URL_3_0 = "jdbc:microsoft:sqlserver://" + IP + ":" + PORT + ";DatabaseName=" + DB_NAME;

    /**
     * Informix 驱动程序
     */
    public static final String INFORMIX_DRIVER = "com.informix.jdbc.IfxDriver";
    /**
     * Informix URL
     */
    public static final String INFORMIX_URL = "jdbc:Informix-sqli://" + IP + ":" + PORT + "/" + DB_NAME + ":INFORMIXSER=myserver";

    /**
     * JDBC-ODBC 驱动程序
     */
    public static final String JDBC_ODBC_DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";
    /**
     * JDBC-ODBC URL
     */
    public static final String JDBC_ODBC_URL = "jdbc:odbc:" + DB_NAME;
    /**
     * H2 驱动程序
     */
    public static final String H2_DRIVER = "org.h2.Driver";
    /**
     * H2(服务式) URL
     */
    public static final String H2_SERVER_URL = "jdbc:h2:tcp://" + IP + "/" + DB_NAME;
    /**
     * H2(嵌入式) URL
     */
    public static final String H2_EMBEDDED_URL = "jdbc:h2:" + DB_NAME;
    /**
     * H2(内存式) URL
     */
    public static final String H2_MEMORY_URL = "jdbc:h2:tcp://" + IP + "/mem:" + DB_NAME;


    /**
     * SQLite 驱动程序
     */
    public static final String SQLITE_DRIVER = "org.sqlite.JDBC";
    /**
     * SQLite(文件式) URL
     */
    public static final String SQLITE_FILE_URL = "jdbc:sqlite:" + DB_NAME;
    /**
     * SQLite(内存式) URL
     */
    public static final String SQLITE_MEMORY_URL = "jdbc:sqlite:memory:myDb";

    /**
     * HSQLDB 驱动程序
     */
    public static final String HSQLDB_DRIVER = "org.hsqldb.jdbcDriver";
    /**
     * HSQLDB(服务器模式	) URL
     */
    public static final String HSQLDB_SERVER_URL = "jdbc:hsqldb:hsql://" + IP + ":" + PORT + "/" + DB_NAME;
    /**
     * HSQLDB(文件式) URL
     */
    public static final String HSQLDB_FILE_URL = "jdbc:hsqldb:file:" + DB_NAME;
    /**
     * HSQLDB(内存式) URL
     */
    public static final String HSQLDB_MEMORY_URL = "jdbc:hsqldb:mem:myDb";
    /**
     * mariadb  URL
     */
    public static final String MARIADB_URL = "jdbc:mariadb://" + IP + ":" + PORT + "/" + DB_NAME;
    /**
     * mariadb  URL
     */
    public static final String MARIADB_DRIVER = "org.mariadb.jdbc.Driver";

    public static final String H2 = "h2";
    public static final String SQLITE = "sqlite";
    public static final String SQLITE_MEMORY = "sqlite-memory";
    public static final String MYSQL = "mysql";
    public static final String ORACLE = "oracle";
    public static final String POSTGRESQL = "postgresql";
    public static final String HSQLDB = "hsqldb";
    public static final String JDBC_ODBC = "jdbc-odbc";
    public static final String INFORMIX = "informix";
    public static final String DB2 = "db2";
    public static final String SYSBASE = "sysbase";
    public static final String SQL_SERVER_2_0 = "sql_server_2_0";
    public static final String SQL_SERVER_3_0 = "sql_server_3_0";
    public static final String MARIADB = "mariadb";

    private static final String DRIVER_CLASS_NAME = "driverClassName";
    private static final String DRIVER = "driver";
    private static final String URL = "url";
    private static final String JDBC_URL = "jdbcUrl";

    /**
     * 猜测数据库类型
     *
     * @param dataSource 数据源
     * @return
     */
    public SQLDialectEnum bestGuessType(DataSource dataSource) {
        if (null == dataSource) {
            return null;
        }
        BeanMap beanMap = BeanMap.create(dataSource);
        Object driver = beanMap.getOrDefault(DRIVER_CLASS_NAME, beanMap.get(DRIVER));
        Object url = beanMap.getOrDefault(URL, beanMap.get(JDBC_URL));

        if (null == driver || "".equals(driver)) {
            return null;
        }

        return SQLDialectEnum.findByDriver(driver.toString(), (String) url);
    }
}

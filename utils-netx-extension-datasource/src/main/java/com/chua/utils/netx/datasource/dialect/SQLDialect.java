package com.chua.utils.netx.datasource.dialect;

/**
 * 数据库方言
 * @author CH
 * @version 1.0.0
 * @since 2020/10/21
 */
public class SQLDialect {
    /**
     * MySQL 驱动程序
     */
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    /**
     * MySQL 链接地址
     * <p>jdbc:mysql://<ip>:<port>/<db_name></p>
     */
    public static final String MYSQL_URL = "jdbc:mysql://<ip>:<port:3306>/<db_name>";
    /**
     * Oracle(thin) 驱动程序
     */
    public static final String ORACLE_THIN_DRIVER = "oracle.jdbc.driver.OracleDriver";
    /**
     * Oracle 链接地址
     */
    public static final String ORACLE_THIN_URL = "jdbc:oracle:thin:@<ip>:<port:1521>:<sid>";
    /**
     * PostgreSQL 驱动程序
     */
    public static final String POSTGRE_SQL_DRIVER = "org.postgresql.Driver";
    /**
     * PostgreSQL 链接地址
     */
    public static final String POSTGRE_SQL_URL = "jdbc:postgresql://<ip>:<port>/<db_name>";
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
    public static final String DB2_URL = "jdbc:db2://<ip>:<port:5000>/<db_name>";
    /**
     * Sysbase 驱动程序
     */
    public static final String SYSBASE_DRIVER = "com.sybase.jdbc.SybDriver";
    /**
     * Sysbase URL
     */
    public static final String SYSBASE_URL = "jdbc:Sysbase://<ip>:<port:5007>/<db_name>";
    /**
     * Microsoft SQL Server 2.0 驱动程序
     */
    public static final String SQL_SERVER_DRIVER_2_0 = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
    /**
     * Microsoft SQL Server 2.0 URL
     */
    public static final String SQL_SERVER_URL_2_0 = "jdbc:microsoft:sqlserver://<ip>:<port:1433>;DatabaseName=<db_name>";
    /**
     * Microsoft SQL Server 3.0 驱动程序
     */
    public static final String SQL_SERVER_DRIVER_3_0 = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    /**
     * Microsoft SQL Server 3.0 URL
     */
    public static final String SQL_SERVER_URL_3_0 = "jdbc:microsoft:sqlserver://<ip>:<port:1433>;DatabaseName=<db_name>";

    /**
     * Informix 驱动程序
     */
    public static final String INFORMIX_DRIVER = "com.informix.jdbc.IfxDriver";
    /**
     * Informix URL
     */
    public static final String INFORMIX_URL = "jdbc:Informix-sqli://<ip>:<port:1533>/<db_name>:INFORMIXSER=myserver";

    /**
     * JDBC-ODBC 驱动程序
     */
    public static final String JDBC_ODBC_DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";
    /**
     * JDBC-ODBC URL
     */
    public static final String JDBC_ODBC_URL = "jdbc:odbc:dbsource";
    /**
     * H2 驱动程序
     */
    public static final String H2_DRIVER = "org.h2.Driver";
    /**
     * H2(服务式) URL
     */
    public static final String H2_SERVER_URL = "jdbc:h2:tcp://<ip>/<db_name>";
    /**
     * H2(嵌入式) URL
     */
    public static final String H2_EMBEDDED_URL = "jdbc:h2:<db_name>";
    /**
     * H2(内存式) URL
     */
    public static final String H2_MEMORY_URL = "jdbc:h2:tcp://<ip>/mem:<db_name>";


    /**
     * SQLite 驱动程序
     */
    public static final String SQLITE_DRIVER = "org.sqlite.JDBC";
    /**
     * SQLite(文件式) URL
     */
    public static final String SQLITE_FILE_URL = "jdbc:sqlite:<db_name>";
    /**
     * SQLite(内存式) URL
     */
    public static final String SQLITE_MEMORY_URL = ":memory:";

    /**
     * HSQLDB 驱动程序
     */
    public static final String HSQLDB_DRIVER = "org.sqlite.JDBC";
    /**
     * HSQLDB(服务器模式	) URL
     */
    public static final String HSQLDB_SERVER_URL = "jdbc:hsqldb:hsql://<ip>:<port:9001>/<db_name>";
    /**
     * HSQLDB(文件式) URL
     */
    public static final String HSQLDB_FILE_URL = "jdbc:hsqldb:file:<db_name>";
    /**
     * HSQLDB(内存式) URL
     */
    public static final String HSQLDB_MEMORY_URL = "jdbc:hsqldb:mem:<db_name>";

}

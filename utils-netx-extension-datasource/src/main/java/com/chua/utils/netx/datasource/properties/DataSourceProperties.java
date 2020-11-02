package com.chua.utils.netx.datasource.properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 数据库参数
 *
 * @author CH
 * @version 1.0
 * @date 2020/10/19 20:06
 */
@Getter
@Setter
public class DataSourceProperties {

    /**
     * 此属性仅通过编程配置或IoC容器可用。
     * 这个属性允许你直接设置DataSource池的实例，而不是让HikariCP通过反射来构造它。
     * 这在一些依赖注入框架中可能很有用。
     * 当指定此属性时，dataSourceClassName属性和所有DataSource特定的属性将被忽略。 默认值：无
     */
    private DataSource dataSource;
    /**
     * 该属性设置默认目录为支持目录的概念数据库。
     * 如果未指定此属性，则使用由JDBC驱动程序定义的默认目录。 默认：驱动程序默认
     */
    private volatile String catalog;
    /**
     * 连接超时时间。默认值为30s，可以接收的最小超时时间为250ms
     */
    private volatile long connectionTimeout = SECONDS.toMillis(30);
    /**
     * 此属性控制连接测试活动的最长时间。
     * 这个值必须小于connectionTimeout。
     * 最低可接受的验证超时时间为250 ms。 默认值：5000
     */
    private volatile long validationTimeout = SECONDS.toMillis(5);
    /**
     * 空闲时间。仅在minimum-idle小于maximum-poop-size的时候才会起作用。默认: 10分钟
     */
    private volatile long idleTimeout = MINUTES.toMillis(10);
    /**
     * 此属性控制在记录消息之前连接可能离开池的时间量，表明可能存在连接泄漏。
     * 值为0意味着泄漏检测被禁用。
     * 启用泄漏检测的最低可接受值为2000（2秒）。 默认值：0
     */
    private volatile long leakDetectionThreshold;
    /**
     * 连接池中连接的最大生命周期: 默认: 30分钟
     */
    private volatile long maxLifetime = MINUTES.toMillis(30);
    /**
     * 池中最大连接数（包括空闲和正在使用的连接）。默认值: 10
     */
    private volatile int maxPoolSize = 10;
    /**
     * 池中最小空闲连接数量。默认: 10
     */
    private volatile int minIdle = 10;
    /**
     * 此属性设置从基础驱动程序获取连接时使用的默认身份验证用户名。
     * 请注意，对于DataSources，这通过调用DataSource.getConnection(username, password)
     * 基础DataSource 以非常确定的方式工作。但是，对于基于驱动程序的配置，每个驱动程序都不同。
     * 在基于驱动程序的情况下，HikariCP将使用此username属性来设置传递给驱动程序调用的user属性。
     * 如果这不是你所需要的，例如完全跳过这个方法并且调用。 默认值：无
     */
    private volatile String username;
    /**
     * 此属性设置从基础驱动程序获取连接时使用的默认身份验证密码。
     * 请注意，对于DataSources，这通过调用DataSource.getConnection(username, password)
     * 基础DataSource 以非常确定的方式工作。但是，对于基于驱动程序的配置，每个驱动程序都不同。
     * 在基于驱动程序的情况下，HikariCP将使用此password属性来设置传递给驱动程序调用的password属性。
     * 如果这不是你所需要的，例如完全跳过这个方法并且调用。 默认值：无
     */
    private volatile String password;

    // Properties NOT changeable at runtime
    //
    private long initializationFailTimeout;
    /**
     * 该属性设置一个SQL语句，在将每个新连接创建后，将其添加到池中之前执行该语句。
     * 如果这个SQL无效或引发异常，它将被视为连接失败并且将遵循标准重试逻辑。 默认值：无
     */
    private String connectionInitSql;
    /**
     * 如果您的驱动程序支持JDBC4，我们强烈建议您不要设置此属性。这是针对不支持JDBC4的“传统”驱动程序Connection.isValid() API
     */
    private String connectionTestQuery;
    /**
     * 这是DataSourceJDBC驱动程序提供的类的名称。请查阅您的特定JDBC驱动程序的文档以获取此类名称，
     * 或参阅下表。注XA数据源不受支持。
     * XA需要像bitronix这样的真正的事务管理器 。
     * 请注意，如果您正在使用jdbcUrl"旧式"基于DriverManager的JDBC驱动程序配置，则不需要此属性 。
     * 默认值：无
     */
    private String dataSourceClassName;
    /**
     * 设置JNDI
     */
    private String dataSourceJndiName;
    /**
     * HikariCP将尝试通过DriverManager仅基于驱动程序来解析驱动程序jdbcUrl，
     * 但对于一些较旧的驱动程序，driverClassName还必须指定它。
     * 除非您收到明显的错误消息，指出找不到驱动程序，否则请忽略此属性。 默认值：无
     */
    private String driverClassName;
    /**
     * 异常重写类名
     */
    private String exceptionOverrideClassName;
    /**
     * 该属性指示HikariCP使用“基于DriverManager的”配置。
     * 我们认为基于DataSource的配置（上图）由于各种原因（参见下文）是优越的，
     * 但对于许多部署来说，几乎没有显着差异。
     * 在“旧”驱动程序中使用此属性时，您可能还需要设置该 driverClassName属性，但不要先尝试。
     * 请注意，如果使用此属性，您仍然可以使用DataSource属性来配置您的驱动程序，
     * 实际上建议您使用URL本身中指定的驱动程序参数。 默认值：无
     */
    private String jdbcUrl;
    /**
     * //连接池的名字。一般会出现在日志和JMX控制台中,默认: datasource-operator
     */
    private String poolName = "datasource-operator";
    /**
     * 该属性设置的默认模式为支持模式的概念数据库。
     * 如果未指定此属性，则使用由JDBC驱动程序定义的默认模式。
     * 默认：驱动程序默认
     */
    private String schema;
    /**
     * 此属性控制从池返回的连接的默认事务隔离级别。
     * 如果未指定此属性，则使用由JDBC驱动程序定义的默认事务隔离级别。
     * 如果您有针对所有查询通用的特定隔离要求，请仅使用此属性。
     * 此属性的值是从不断的名称Connection 类，
     * 如TRANSACTION_READ_COMMITTED，TRANSACTION_REPEATABLE_READ等 默认值：驱动程序默认
     */
    private String transactionIsolation;
    /**
     * 是否自动提交池中返回的连接。默认值为true。
     */
    private boolean isAutoCommit = true;
    /**
     * 此属性控制默认情况下从池中获取的连接是否处于只读模式。
     * 注意某些数据库不支持只读模式的概念，而其他数据库则在Connection设置为只读时提供查询优化。
     * 无论您是否需要此属性，都将主要取决于您的应用程序和数据库。 默认值：false
     */
    private boolean isReadOnly;
    /**
     * 隔离内部查询
     */
    private boolean isIsolateInternalQueries;
    /**
     * 该属性控制是否注册JMX管理Bean("MBeans")
     */
    private boolean isRegisterMbeans;
    private boolean isAllowPoolSuspension;
    /**
     * DataSource参数
     */
    private Properties dataSourceProperties;
    /**
     * 此属性仅通过编程配置或IoC容器可用。
     * 该属性允许您设置java.util.concurrent.ThreadFactory将用于创建池使用的所有线程的实例。
     * 在一些只能通过ThreadFactory应用程序容器提供的线程创建线程的有限执行环境中需要它。 默认值：无
     */
    private ThreadFactory threadFactory;
    /**
     * 此属性仅通过编程配置或IoC容器可用。
     * 该属性允许您设置java.util.concurrent.ScheduledExecutorService将用于各种内部计划任务的实例。
     * 如果为ScheduledThreadPoolExecutor 实例提供HikariCP，建议setRemoveOnCancelPolicy(true)使用它。
     * 默认值：无
     */
    private ScheduledExecutorService scheduledExecutor;
    private MetricsTrackerFactory metricsTrackerFactory;
    /**
     * 该属性仅通过编程配置或IoC容器可用。该属性允许您指定池使用的Codahale / Dropwizard 实例MetricRegistry来记录各种指标
     */
    private Object metricRegistry;
    /**
     * 该属性仅通过编程配置或IoC容器可用。该属性允许您指定池使用的Codahale / Dropwizard 的实例HealthCheckRegistry来报告当前的健康信息
     */
    private Object healthCheckRegistry;
    /**
     * HealthCheck参数
     */
    private Properties healthCheckProperties;

    private volatile boolean sealed;

    /**
     * 检验 catalog. 的值是否有效
     *
     * @return catalog 值有效返回true, 反之false
     */
    public boolean isCatalogValid() {
        return null != catalog;
    }

    /**
     * 检验 connectionTimeout. 的值是否有效
     *
     * @return connectionTimeout 值有效返回true, 反之false
     */
    public boolean isConnectionTimeoutValid() {
        return connectionTimeout > 0;
    }

    /**
     * 检验 validationTimeout. 的值是否有效
     *
     * @return validationTimeout 值有效返回true, 反之false
     */
    public boolean isValidationTimeoutValid() {
        return validationTimeout > 0;
    }

    /**
     * 检验 idleTimeout. 的值是否有效
     *
     * @return idleTimeout 值有效返回true, 反之false
     */
    public boolean isIdleTimeoutValid() {
        return idleTimeout > 0;
    }

    /**
     * 检验 leakDetectionThreshold. 的值是否有效
     *
     * @return leakDetectionThreshold 值有效返回true, 反之false
     */
    public boolean isLeakDetectionThresholdValid() {
        return leakDetectionThreshold > 0;
    }

    /**
     * 检验 maxLifetime. 的值是否有效
     *
     * @return maxLifetime 值有效返回true, 反之false
     */
    public boolean isMaxLifetimeValid() {
        return maxLifetime > 0;
    }

    /**
     * 检验 maxPoolSize. 的值是否有效
     *
     * @return maxPoolSize 值有效返回true, 反之false
     */
    public boolean isMaxPoolSizeValid() {
        return maxPoolSize > 0;
    }

    /**
     * 检验 minIdle. 的值是否有效
     *
     * @return minIdle 值有效返回true, 反之false
     */
    public boolean isMinIdleValid() {
        return minIdle > 0;
    }

    /**
     * 检验 username. 的值是否有效
     *
     * @return username 值有效返回true, 反之false
     */
    public boolean isUsernameValid() {
        return null != username;
    }

    /**
     * 检验 password. 的值是否有效
     *
     * @return password 值有效返回true, 反之false
     */
    public boolean isPasswordValid() {
        return null != password;
    }

    /**
     * 检验 initializationFailTimeout. 的值是否有效
     *
     * @return initializationFailTimeout 值有效返回true, 反之false
     */
    public boolean isInitializationFailTimeoutValid() {
        return initializationFailTimeout > 0;
    }

    /**
     * 检验 connectionInitSql. 的值是否有效
     *
     * @return connectionInitSql 值有效返回true, 反之false
     */
    public boolean isConnectionInitSqlValid() {
        return null != connectionInitSql;
    }

    /**
     * 检验 connectionTestQuery. 的值是否有效
     *
     * @return connectionTestQuery 值有效返回true, 反之false
     */
    public boolean isConnectionTestQueryValid() {
        return null != connectionTestQuery;
    }

    /**
     * 检验 dataSourceClassName. 的值是否有效
     *
     * @return dataSourceClassName 值有效返回true, 反之false
     */
    public boolean isDataSourceClassNameValid() {
        return null != dataSourceClassName;
    }

    /**
     * 检验 dataSourceJndiName. 的值是否有效
     *
     * @return dataSourceJndiName 值有效返回true, 反之false
     */
    public boolean isDataSourceJndiNameValid() {
        return null != dataSourceJndiName;
    }

    /**
     * 检验 driverClassName. 的值是否有效
     *
     * @return driverClassName 值有效返回true, 反之false
     */
    public boolean isDriverClassNameValid() {
        return null != driverClassName;
    }

    /**
     * 检验 exceptionOverrideClassName. 的值是否有效
     *
     * @return exceptionOverrideClassName 值有效返回true, 反之false
     */
    public boolean isExceptionOverrideClassNameValid() {
        return null != exceptionOverrideClassName;
    }

    /**
     * 检验 jdbcUrl. 的值是否有效
     *
     * @return jdbcUrl 值有效返回true, 反之false
     */
    public boolean isJdbcUrlValid() {
        return null != jdbcUrl;
    }

    /**
     * 检验 poolName. 的值是否有效
     *
     * @return poolName 值有效返回true, 反之false
     */
    public boolean isPoolNameValid() {
        return null != poolName;
    }

    /**
     * 检验 schema. 的值是否有效
     *
     * @return schema 值有效返回true, 反之false
     */
    public boolean isSchemaValid() {
        return null != schema;
    }

    /**
     * 检验 transactionIsolation. 的值是否有效
     *
     * @return transactionIsolation 值有效返回true, 反之false
     */
    public boolean isTransactionIsolationValid() {
        return null != transactionIsolation;
    }

    /**
     * 检验 isAutoCommit. 的值是否有效
     *
     * @return isAutoCommit 值有效返回true, 反之false
     */
    public boolean isAutoCommitValid() {
        return isAutoCommit;
    }

    /**
     * 检验 isReadOnly. 的值是否有效
     *
     * @return isReadOnly 值有效返回true, 反之false
     */
    public boolean isReadOnlyValid() {
        return isReadOnly;
    }

    /**
     * 检验 isIsolateInternalQueries. 的值是否有效
     *
     * @return isIsolateInternalQueries 值有效返回true, 反之false
     */
    public boolean isIsolateInternalQueriesValid() {
        return isIsolateInternalQueries;
    }

    /**
     * 检验 isRegisterMbeans. 的值是否有效
     *
     * @return isRegisterMbeans 值有效返回true, 反之false
     */
    public boolean isRegisterMbeansValid() {
        return isRegisterMbeans;
    }

    /**
     * 检验 isAllowPoolSuspension. 的值是否有效
     *
     * @return isAllowPoolSuspension 值有效返回true, 反之false
     */
    public boolean isAllowPoolSuspensionValid() {
        return isAllowPoolSuspension;
    }

    /**
     * 检验 dataSource. 的值是否有效
     *
     * @return dataSource 值有效返回true, 反之false
     */
    public boolean isDataSourceValid() {
        return null != dataSource;
    }

    /**
     * 检验 dataSourceProperties. 的值是否有效
     *
     * @return dataSourceProperties 值有效返回true, 反之false
     */
    public boolean isDataSourcePropertiesValid() {
        return null != dataSourceProperties;
    }

    /**
     * 检验 threadFactory. 的值是否有效
     *
     * @return threadFactory 值有效返回true, 反之false
     */
    public boolean isThreadFactoryValid() {
        return null != threadFactory;
    }

    /**
     * 检验 scheduledExecutor. 的值是否有效
     *
     * @return scheduledExecutor 值有效返回true, 反之false
     */
    public boolean isScheduledExecutorValid() {
        return null != scheduledExecutor;
    }

    /**
     * 检验 metricsTrackerFactory. 的值是否有效
     *
     * @return metricsTrackerFactory 值有效返回true, 反之false
     */
    public boolean isMetricsTrackerFactoryValid() {
        return null != metricsTrackerFactory;
    }

    /**
     * 检验 metricRegistry. 的值是否有效
     *
     * @return metricRegistry 值有效返回true, 反之false
     */
    public boolean isMetricRegistryValid() {
        return null != metricRegistry;
    }

    /**
     * 检验 healthCheckRegistry. 的值是否有效
     *
     * @return healthCheckRegistry 值有效返回true, 反之false
     */
    public boolean isHealthCheckRegistryValid() {
        return null != healthCheckRegistry;
    }

    /**
     * 检验 healthCheckProperties. 的值是否有效
     *
     * @return healthCheckProperties 值有效返回true, 反之false
     */
    public boolean isHealthCheckPropertiesValid() {
        return null != healthCheckProperties;
    }

    /**
     * 检验 sealed. 的值是否有效
     *
     * @return sealed 值有效返回true, 反之false
     */
    public boolean isSealedValid() {
        return sealed;
    }


}

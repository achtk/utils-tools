package com.chua.utils.netx.datasource.transform;

import com.chua.utils.netx.datasource.properties.DataSourceProperties;
import com.chua.utils.tools.properties.OperatorProperties;
import com.chua.utils.tools.transform.OperatorTransform;
import com.google.common.base.Strings;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.cglib.beans.BeanMap;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 操作转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
@SuppressWarnings("ALL")
public class JdbcOperatorTransform implements OperatorTransform<DataSource> {

    private HikariConfig hikariConfig;

    @Override
    public DataSource transform(OperatorProperties operatorProperties) {
        this.hikariConfig = new HikariConfig();
        Properties properties = operatorProperties.properties();

        if (null != properties) {
            packageAdditionalParameters(properties);
        }

        hikariConfig.setJdbcUrl(operatorProperties.url());
        hikariConfig.setDriverClassName(operatorProperties.driver());

        if (!Strings.isNullOrEmpty(operatorProperties.username())) {
            hikariConfig.setUsername(operatorProperties.username());
        }

        if (!Strings.isNullOrEmpty(operatorProperties.password())) {
            hikariConfig.setPassword(operatorProperties.password());
        }

        return new HikariDataSource(hikariConfig);
    }

    /**
     * 封装额外参数
     *
     * @param properties 额外参数
     */
    private void packageAdditionalParameters(Properties properties) {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        BeanMap beanMap = BeanMap.create(dataSourceProperties);
        beanMap.putAll(properties);
        dataSourceProperties = (DataSourceProperties) beanMap.getBean();
        //池中最大连接数（包括空闲和正在使用的连接）。默认值: 10
        if (dataSourceProperties.isMaxPoolSizeValid()) {
            hikariConfig.setMaximumPoolSize(dataSourceProperties.getMaxPoolSize());
        }
        //池中最小空闲连接数量。默认: 10
        if (dataSourceProperties.isMinIdleValid()) {
            hikariConfig.setMinimumIdle(dataSourceProperties.getMinIdle());
        }
        //连接池的名字。一般会出现在日志和JMX控制台中,默认: datasource-operator
        if (dataSourceProperties.isPoolNameValid()) {
            hikariConfig.setPoolName(dataSourceProperties.getPoolName());
        }
        //是否自动提交池中返回的连接。默认值为true。
        hikariConfig.setAutoCommit(dataSourceProperties.isAutoCommit());
        //空闲时间。仅在minimum-idle小于maximum-poop-size的时候才会起作用。默认值10分钟
        if (dataSourceProperties.isIdleTimeoutValid()) {
            hikariConfig.setIdleTimeout(dataSourceProperties.getIdleTimeout());
        }
        //连接池中连接的最大生命周期: 默认: 30分钟
        if (dataSourceProperties.isMaxLifetimeValid()) {
            hikariConfig.setMaxLifetime(dataSourceProperties.getMaxLifetime());
        }
        //连接超时时间。默认值为30s，可以接收的最小超时时间为250ms
        if (dataSourceProperties.isConnectionTimeoutValid()) {
            hikariConfig.setConnectionTimeout(dataSourceProperties.getConnectionTimeout());
        }
        //验证超时。默认值: 5S, 如果小于250毫秒，则会被重置回5秒
        if (dataSourceProperties.isValidationTimeoutValid()) {
            hikariConfig.setValidationTimeout(dataSourceProperties.getValidationTimeout());
        }
        //如果您的驱动程序支持JDBC4，我们强烈建议您不要设置此属性。这是针对不支持JDBC4的“传统”驱动程序Connection.isValid() API
        if (dataSourceProperties.isConnectionTestQueryValid()) {
            hikariConfig.setConnectionTestQuery(dataSourceProperties.getConnectionTestQuery());
        }
        //该属性仅通过编程配置或IoC容器可用。该属性允许您指定池使用的Codahale / Dropwizard 实例MetricRegistry来记录各种指标
        if (dataSourceProperties.isMetricRegistryValid()) {
            hikariConfig.setMetricRegistry(dataSourceProperties.getMetricRegistry());
        }
        //该属性仅通过编程配置或IoC容器可用。该属性允许您指定池使用的Codahale / Dropwizard 的实例HealthCheckRegistry来报告当前的健康信息
        if (dataSourceProperties.isHealthCheckRegistryValid()) {
            hikariConfig.setHealthCheckRegistry(dataSourceProperties.getHealthCheckRegistry());
        }
        //该属性设置默认目录为支持目录的概念数据库。如果未指定此属性，则使用由JDBC驱动程序定义的默认目录。 默认：驱动程序默认
        if (dataSourceProperties.isCatalogValid()) {
            hikariConfig.setCatalog(dataSourceProperties.getCatalog());
        }
        //该属性设置一个SQL语句，在将每个新连接创建后，将其添加到池中之前执行该语句。如果这个SQL无效或引发异常，它将被视为连接失败并且将遵循标准重试逻辑。 默认值：无
        if (dataSourceProperties.isConnectionInitSqlValid()) {
            hikariConfig.setConnectionInitSql(dataSourceProperties.getConnectionInitSql());
        }
        //此属性控制从池返回的连接的默认事务隔离级别。如果未指定此属性，则使用由JDBC驱动程序定义的默认事务隔离级别。如果您有针对所有查询通用的特定隔离要求，请仅使用此属性。此属性的值是从不断的名称Connection 类，如TRANSACTION_READ_COMMITTED，TRANSACTION_REPEATABLE_READ等 默认值：驱动程序默认
        if (dataSourceProperties.isTransactionIsolationValid()) {
            hikariConfig.setTransactionIsolation(dataSourceProperties.getTransactionIsolation());
        }
        //此属性控制在记录消息之前连接可能离开池的时间量，表明可能存在连接泄漏。值为0意味着泄漏检测被禁用。启用泄漏检测的最低可接受值为2000（2秒）。 默认值：0
        if (dataSourceProperties.isLeakDetectionThresholdValid()) {
            hikariConfig.setLeakDetectionThreshold(dataSourceProperties.getLeakDetectionThreshold());
        }
        //此属性仅通过编程配置或IoC容器可用。这个属性允许你直接设置DataSource池的实例，而不是让HikariCP通过反射来构造它。这在一些依赖注入框架中可能很有用。当指定此属性时，dataSourceClassName属性和所有DataSource特定的属性将被忽略。 默认值：无
        if (dataSourceProperties.isDataSourceValid()) {
            hikariConfig.setDataSource(dataSourceProperties.getDataSource());
        }
        //该属性设置的默认模式为支持模式的概念数据库。如果未指定此属性，则使用由JDBC驱动程序定义的默认模式。 默认：驱动程序默认
        if (dataSourceProperties.isSchemaValid()) {
            hikariConfig.setSchema(dataSourceProperties.getSchema());
        }
        //此属性仅通过编程配置或IoC容器可用。该属性允许您设置java.util.concurrent.ThreadFactory将用于创建池使用的所有线程的实例。在一些只能通过ThreadFactory应用程序容器提供的线程创建线程的有限执行环境中需要它。 默认值：无
        if (dataSourceProperties.isThreadFactoryValid()) {
            hikariConfig.setThreadFactory(dataSourceProperties.getThreadFactory());
        }
        //此属性仅通过编程配置或IoC容器可用。该属性允许您设置java.util.concurrent.ScheduledExecutorService将用于各种内部计划任务的实例。如果为ScheduledThreadPoolExecutor 实例提供HikariCP，建议setRemoveOnCancelPolicy(true)使用它。 默认值：无
        if (dataSourceProperties.isScheduledExecutorValid()) {
            hikariConfig.setScheduledExecutor(dataSourceProperties.getScheduledExecutor());
        }
        //HikariCP将尝试通过DriverManager仅基于驱动程序来解析驱动程序jdbcUrl，但对于一些较旧的驱动程序，driverClassName还必须指定它。除非您收到明显的错误消息，指出找不到驱动程序，否则请忽略此属性。 默认值：无
        if (dataSourceProperties.isDriverClassNameValid()) {
            hikariConfig.setDriverClassName(dataSourceProperties.getDriverClassName());
        }
        //这是DataSourceJDBC驱动程序提供的类的名称。请查阅您的特定JDBC驱动程序的文档以获取此类名称，或参阅下表。注XA数据源不受支持。XA需要像bitronix这样的真正的事务管理器 。请注意，如果您正在使用jdbcUrl“旧式”基于DriverManager的JDBC驱动程序配置，则不需要此属性 。 默认值：无
        if (dataSourceProperties.isDataSourceClassNameValid()) {
            hikariConfig.setDataSourceClassName(dataSourceProperties.getDataSourceClassName());
        }
        //该属性指示HikariCP使用“基于DriverManager的”配置。我们认为基于DataSource的配置（上图）由于各种原因（参见下文）是优越的，但对于许多部署来说，几乎没有显着差异。 在“旧”驱动程序中使用此属性时，您可能还需要设置该 driverClassName属性，但不要先尝试。 请注意，如果使用此属性，您仍然可以使用DataSource属性来配置您的驱动程序，实际上建议您使用URL本身中指定的驱动程序参数。 默认值：无
        if (dataSourceProperties.isJdbcUrlValid()) {
            hikariConfig.setJdbcUrl(dataSourceProperties.getJdbcUrl());
        }
        //此属性设置从基础驱动程序获取连接时使用的默认身份验证用户名。请注意，对于DataSources，这通过调用DataSource.getConnection(username, password)基础DataSource 以非常确定的方式工作。但是，对于基于驱动程序的配置，每个驱动程序都不同。在基于驱动程序的情况下，HikariCP将使用此username属性来设置传递给驱动程序调用的user属性。如果这不是你所需要的，例如完全跳过这个方法并且调用。 默认值：无
        if (dataSourceProperties.isUsernameValid()) {
            hikariConfig.setUsername(dataSourceProperties.getUsername());
        }
        //此属性设置从基础驱动程序获取连接时使用的默认身份验证密码。请注意，对于DataSources，这通过调用DataSource.getConnection(username, password)基础DataSource 以非常确定的方式工作。但是，对于基于驱动程序的配置，每个驱动程序都不同。在基于驱动程序的情况下，HikariCP将使用此password属性来设置传递给驱动程序调用的password属性。如果这不是你所需要的，例如完全跳过这个方法并且调用。 默认值：无
        if (dataSourceProperties.isPasswordValid()) {
            hikariConfig.setPassword(dataSourceProperties.getPassword());
        }
        //设置数据库的JNDI
        if (dataSourceProperties.isDataSourceJndiNameValid()) {
            hikariConfig.setDataSourceJNDI(dataSourceProperties.getDataSourceJndiName());
        }
        //异常重写类名
        if (dataSourceProperties.isExceptionOverrideClassNameValid()) {
            hikariConfig.setExceptionOverrideClassName(dataSourceProperties.getExceptionOverrideClassName());
        }
        //DataSource参数
        if (dataSourceProperties.isDataSourcePropertiesValid()) {
            hikariConfig.setDataSourceProperties(dataSourceProperties.getDataSourceProperties());
        }
        //HealthCheck参数
        if (dataSourceProperties.isHealthCheckPropertiesValid()) {
            hikariConfig.setHealthCheckProperties(dataSourceProperties.getHealthCheckProperties());
        }

        //隔离内部查询
        hikariConfig.setIsolateInternalQueries(dataSourceProperties.isIsolateInternalQueries());
        //此属性控制默认情况下从池中获取的连接是否处于只读模式
        hikariConfig.setReadOnly(dataSourceProperties.isReadOnly());
        //该属性控制是否注册JMX管理Bean("MBeans")
        hikariConfig.setRegisterMbeans(dataSourceProperties.isRegisterMbeans());
    }
}

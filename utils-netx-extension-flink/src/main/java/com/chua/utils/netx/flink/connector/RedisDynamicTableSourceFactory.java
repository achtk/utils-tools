package com.chua.utils.netx.flink.connector;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.factories.DynamicTableSinkFactory;
import org.apache.flink.table.factories.FactoryUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * redis source factory
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/21
 */
public class RedisDynamicTableSourceFactory implements DynamicTableSinkFactory {
    public static final String IDENTIFIER = "redis";
    public static final String REDIS_HOST_VALUE = "redis.host";
    public static final String REDIS_PORT_VALUE = "redis.port";
    public static final String REDIS_PASSWORD_VALUE = "redis.password";
    public static final String REDIS_DATABASE_VALUE = "redis.database";
    public static final String REDIS_TIMEOUT_VALUE = "redis.timeout";
    public static final String REDIS_MAX_IDLE_VALUE = "redis.maxIdle";
    public static final String REDIS_MIN_IDLE_VALUE = "redis.minIdle";
    public static final String REDIS_MAX_TOTAL_VALUE = "redis.maxTotal";
    public static final ConfigOption<String> HOST = ConfigOptions.key(REDIS_HOST_VALUE).stringType().defaultValue("localhost");
    public static final ConfigOption<Integer> PORT = ConfigOptions.key(REDIS_PORT_VALUE).intType().defaultValue(6349);
    public static final ConfigOption<String> PASSWORD = ConfigOptions.key(REDIS_PASSWORD_VALUE).stringType().noDefaultValue();
    public static final ConfigOption<Integer> DATABASE = ConfigOptions.key(REDIS_DATABASE_VALUE).intType().defaultValue(0);
    public static final ConfigOption<Integer> TIMEOUT = ConfigOptions.key(REDIS_TIMEOUT_VALUE).intType().noDefaultValue();
    public static final ConfigOption<Integer> MAX_IDLE = ConfigOptions.key(REDIS_MAX_IDLE_VALUE).intType().noDefaultValue();
    public static final ConfigOption<Integer> MIN_IDLE = ConfigOptions.key(REDIS_MIN_IDLE_VALUE).intType().noDefaultValue();
    public static final ConfigOption<Integer> MAX_TOTAL = ConfigOptions.key(REDIS_MAX_TOTAL_VALUE).intType().noDefaultValue();

    @Override
    public DynamicTableSink createDynamicTableSink(Context context) {
        final FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);
        helper.validate();
        ReadableConfig options = helper.getOptions();
        return new RedisDynamicTableSink(options);
    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        return options;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(PORT);
        options.add(HOST);
        options.add(PASSWORD);
        options.add(DATABASE);
        options.add(TIMEOUT);
        options.add(MAX_IDLE);
        options.add(MAX_TOTAL);
        options.add(MIN_IDLE);
        return options;
    }
}

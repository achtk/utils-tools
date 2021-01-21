package com.chua.utils.netx.flink.connector;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.factories.DynamicTableSinkFactory;
import org.apache.flink.table.factories.DynamicTableSourceFactory;
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
public class RedisDynamicTableSourceFactory implements DynamicTableSourceFactory, DynamicTableSinkFactory {
    public static final String IDENTIFIER = "redis";
    public static final ConfigOption<String> HOST = ConfigOptions.key("redis.host").stringType().defaultValue("localhost");
    public static final ConfigOption<Integer> PORT = ConfigOptions.key("redis.port").intType().defaultValue(6349);
    public static final ConfigOption<String> PASSWORD = ConfigOptions.key("redis.password").stringType().noDefaultValue();
    public static final ConfigOption<Integer> DATABASE = ConfigOptions.key("redis.database").intType().defaultValue(0);
    public static final ConfigOption<Integer> TIMEOUT = ConfigOptions.key("redis.timeout").intType().noDefaultValue();
    public static final ConfigOption<Integer> MAX_IDLE = ConfigOptions.key("redis.maxIdle").intType().noDefaultValue();
    public static final ConfigOption<Integer> MIN_IDLE = ConfigOptions.key("redis.minIdle").intType().noDefaultValue();
    public static final ConfigOption<Integer> MAX_TOTAL = ConfigOptions.key("redis.maxTotal").intType().noDefaultValue();

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
        Set<ConfigOption<?>> options = new HashSet();
        options.add(HOST);
        return options;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> options = new HashSet();
        options.add(PORT);
        options.add(PASSWORD);
        options.add(DATABASE);
        options.add(TIMEOUT);
        options.add(MAX_IDLE);
        options.add(MAX_TOTAL);
        options.add(MIN_IDLE);
        return options;
    }

    @Override
    public DynamicTableSource createDynamicTableSource(Context context) {
        return null;
    }
}

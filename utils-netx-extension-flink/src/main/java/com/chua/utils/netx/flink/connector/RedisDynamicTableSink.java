package com.chua.utils.netx.flink.connector;

import com.google.common.base.Strings;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.sink.SinkFunctionProvider;
import org.apache.flink.table.data.RowData;

/**
 * redis table sink
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/21
 */
public class RedisDynamicTableSink implements DynamicTableSink {

    private ReadableConfig options;

    public RedisDynamicTableSink(ReadableConfig options) {
        this.options = options;
    }

    @Override
    public ChangelogMode getChangelogMode(ChangelogMode changelogMode) {
        return ChangelogMode.insertOnly();
    }

    @Override
    public SinkRuntimeProvider getSinkRuntimeProvider(Context context) {
        String host = options.get(RedisDynamicTableSourceFactory.HOST);
        FlinkJedisPoolConfig.Builder builder = new FlinkJedisPoolConfig.Builder().setHost(host);
        Integer port = options.get(RedisDynamicTableSourceFactory.PORT);
        Integer database = options.get(RedisDynamicTableSourceFactory.DATABASE);
        Integer maxIdle = options.get(RedisDynamicTableSourceFactory.MAX_IDLE);
        Integer minIdle = options.get(RedisDynamicTableSourceFactory.MIN_IDLE);
        Integer maxTotal = options.get(RedisDynamicTableSourceFactory.MAX_TOTAL);
        Integer timeout = options.get(RedisDynamicTableSourceFactory.TIMEOUT);
        String pwd = options.get(RedisDynamicTableSourceFactory.PASSWORD);

        if (port != null) {
            builder.setPort(port);
        }

        if (database != null) {
            builder.setDatabase(database);
        }

        if (timeout != null) {
            builder.setTimeout(timeout);
        }

        if (maxIdle != null) {
            builder.setMaxIdle(maxIdle);
        }

        if (minIdle != null) {
            builder.setMinIdle(minIdle);
        }

        if (maxTotal != null) {
            builder.setMaxTotal(maxTotal);
        }

        if (!Strings.isNullOrEmpty(pwd)) {
            builder.setPassword(pwd);
        }

        FlinkJedisPoolConfig build = builder.build();
        RedisMapper<RowData> stringRedisMapper = new RedisMapper<RowData>() {
            @Override
            public RedisCommandDescription getCommandDescription() {
                return new RedisCommandDescription(RedisCommand.SET);
            }

            @Override
            public String getKeyFromData(RowData rowData) {
                return rowData.getString(0).toString();
            }

            @Override
            public String getValueFromData(RowData rowData) {
                return rowData.toString();
            }
        };
        RedisSink<RowData> stringRedisSink = new RedisSink<>(build, stringRedisMapper);
        return SinkFunctionProvider.of(stringRedisSink);
    }

    @Override
    public DynamicTableSink copy() {
        return new RedisDynamicTableSink(this.options);
    }

    @Override
    public String asSummaryString() {
        return "my_redis_sink";
    }

}

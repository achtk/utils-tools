package com.chua.utils.netx.flink.connector.factory;

import com.chua.utils.netx.flink.format.*;
import com.google.auto.service.AutoService;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.descriptors.DescriptorProperties;
import org.apache.flink.table.factories.TableFactory;

/**
 * redis table factory
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
@AutoService(TableFactory.class)
public class RedisFlinkTableFactory implements FlinkTableFactory {
    @Override
    public String connectorType() {
        return "redis";
    }

    @Override
    public String[] supportedConfig() {
        return new String[]{
                "redis.host",
                "redis.index",
                "redis.port",
                "redis.password",
                "redis.database",
                "redis.maxIdle",
                "redis.minIdle",
                "redis.maxTotal",
                "redis.data.delimiter",
                "redis.kv.delimiter",
                "schema.#.name",
                "schema.#.data-type"
        };
    }

    @Override
    public FlinkInputFormat createInputFormat() {
        return new RedisInputFormat();
    }

    @Override
    public FlinkOutputFormat createOutputFormat() {
        return new RedisOutputFormat();
    }

    @Override
    public void createConnector(String sign, TableSchema schema, DescriptorProperties descriptorProperties) {
        if (!FormatConnector.container(sign)) {
            FormatConnector.setIndex(sign, descriptorProperties.getOptionalString("redis.index").orElse("demo"));
            FormatConnector.setKvDelimiter(sign, descriptorProperties.getOptionalString("redis.kv.delimiter").orElse(":"));
            FormatConnector.setDataDelimiter(sign, descriptorProperties.getOptionalString("redis.data.delimiter").orElse(","));
            FlinkTableFactory.super.createConnector(sign, schema, descriptorProperties);
        }
    }
}

package com.chua.utils.netx.flink.connector;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.descriptors.DescriptorProperties;
import org.apache.flink.table.descriptors.SchemaValidator;
import org.apache.flink.table.factories.StreamTableSinkFactory;
import org.apache.flink.table.factories.StreamTableSourceFactory;
import org.apache.flink.table.sinks.StreamTableSink;
import org.apache.flink.table.sources.StreamTableSource;
import org.apache.flink.table.utils.TableSchemaUtils;
import org.apache.flink.types.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chua.utils.netx.flink.connector.RedisDynamicTableSourceFactory.*;
import static org.apache.flink.table.descriptors.ConnectorDescriptorValidator.CONNECTOR_PROPERTY_VERSION;
import static org.apache.flink.table.descriptors.ConnectorDescriptorValidator.CONNECTOR_TYPE;
import static org.apache.flink.table.descriptors.Schema.*;

/**
 * redis table source sink
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/21
 */
public class RedisTableSourceSinkFactory implements
        StreamTableSourceFactory<Row>,
        StreamTableSinkFactory<Tuple2<Boolean, Row>> {
    @Override
    public Map<String, String> requiredContext() {
        Map<String, String> context = new HashMap<>();
        context.put(CONNECTOR_TYPE, IDENTIFIER);
        context.put(CONNECTOR_PROPERTY_VERSION, "1");
        return context;
    }

    @Override
    public List<String> supportedProperties() {
        List<String> properties = new ArrayList<>();
        properties.add(REDIS_HOST_VALUE);
        properties.add(REDIS_PORT_VALUE);
        properties.add(REDIS_PASSWORD_VALUE);
        properties.add(REDIS_DATABASE_VALUE);
        properties.add(REDIS_MAX_IDLE_VALUE);
        properties.add(REDIS_MAX_TOTAL_VALUE);
        properties.add(REDIS_MIN_IDLE_VALUE);

        // schema
        properties.add(SCHEMA + ".#." + SCHEMA_DATA_TYPE);
        properties.add(SCHEMA + ".#." + SCHEMA_TYPE);
        properties.add(SCHEMA + ".#." + SCHEMA_NAME);
        return properties;
    }


    @Override
    public StreamTableSource<Row> createStreamTableSource(Map<String, String> properties) {
        DescriptorProperties descriptorProperties = getValidatedProperties(properties);
        TableSchema schema = TableSchemaUtils.getPhysicalSchema(descriptorProperties.getTableSchema(SCHEMA));
        RedisInputFormat redisInputFormat = new RedisInputFormat(properties);
        return new RedisStreamTableSource(schema, redisInputFormat);
    }


    @Override
    public StreamTableSink<Tuple2<Boolean, Row>> createStreamTableSink(Map<String, String> properties) {
        DescriptorProperties descriptorProperties = getValidatedProperties(properties);
        TableSchema schema = TableSchemaUtils.getPhysicalSchema(descriptorProperties.getTableSchema(SCHEMA));

        RedisOutFormat redisOutFormat = new RedisOutFormat(properties);
        RedisStreamTableSink redisRetractTableSink = new RedisStreamTableSink(redisOutFormat);
        redisRetractTableSink.setFieldNames(schema.getFieldNames());
        redisRetractTableSink.setFieldTypes(schema.getFieldDataTypes());

        return (StreamTableSink) redisRetractTableSink.configure(schema.getFieldNames(), schema.getFieldTypes());
    }


    private DescriptorProperties getValidatedProperties(Map<String, String> properties) {
        final DescriptorProperties descriptorProperties = new DescriptorProperties(true);
        descriptorProperties.putProperties(properties);
        new SchemaValidator(true, false, false).validate(descriptorProperties);
        return descriptorProperties;
    }

}

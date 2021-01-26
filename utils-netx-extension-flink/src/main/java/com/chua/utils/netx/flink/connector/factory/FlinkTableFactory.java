package com.chua.utils.netx.flink.connector.factory;

import com.chua.utils.netx.flink.connector.FactorySourceConnector;
import com.chua.utils.netx.flink.connector.StandardTableSink;
import com.chua.utils.netx.flink.connector.StandardTableSource;
import com.chua.utils.netx.flink.format.FlinkInputFormat;
import com.chua.utils.netx.flink.format.FlinkOutputFormat;
import com.chua.utils.tools.text.IdHelper;
import com.google.common.collect.Lists;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.descriptors.DescriptorProperties;
import org.apache.flink.table.factories.StreamTableSinkFactory;
import org.apache.flink.table.factories.StreamTableSourceFactory;
import org.apache.flink.table.sinks.TableSink;
import org.apache.flink.table.sources.TableSource;
import org.apache.flink.table.utils.TableSchemaUtils;
import org.apache.flink.types.Row;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * flink table factory
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
public interface FlinkTableFactory extends FactorySourceConnector, StreamTableSourceFactory<Object>, StreamTableSinkFactory<Row> {
    /**
     * 标识
     */
    String sign = IdHelper.createUuid();

    /**
     * 连接类型
     * <p>connector.type = </p>
     *
     * @return 连接类型
     */
    String connectorType();

    /**
     * 必须的配置
     *
     * @return 必须的配置
     */
    default Map<String, String> requiredConfig() {
        return null;
    }

    /**
     * 选填的配置
     *
     * @return 选填的配置
     */
    default String[] supportedConfig() {
        return new String[0];
    }

    /**
     * 创建输入
     *
     * @return FlinkInputFormat
     */
    FlinkInputFormat createInputFormat();

    /**
     * 创建输出
     *
     * @return FlinkOutputFormat
     */
    FlinkOutputFormat createOutputFormat();

    /**
     * TableSink
     *
     * @param properties 配置
     * @return TableSink
     */
    @Override
    default TableSink<Row> createTableSink(Map<String, String> properties) {
        DescriptorProperties descriptorProperties = new DescriptorProperties(true);
        descriptorProperties.putProperties(properties);
        TableSchema schema = TableSchemaUtils.getPhysicalSchema(descriptorProperties.getTableSchema("schema"));
        this.createConnector(sign, schema, descriptorProperties);

        return new StandardTableSink(sign, schema, createOutputFormat());
    }

    /**
     * TableSource
     *
     * @param properties 配置
     * @return TableSource
     */
    @Override
    default TableSource<Object> createTableSource(Map<String, String> properties) {
        DescriptorProperties descriptorProperties = new DescriptorProperties(true);
        descriptorProperties.putProperties(properties);
        TableSchema schema = TableSchemaUtils.getPhysicalSchema(descriptorProperties.getTableSchema("schema"));

        this.createConnector(sign, schema, descriptorProperties);

        return new StandardTableSource(sign, schema, createInputFormat());
    }

    /**
     * 创建属性
     *
     * @param sign                 标识
     * @param schema               schema
     * @param descriptorProperties 属性
     */
    @Override
    default void createConnector(String sign, TableSchema schema, DescriptorProperties descriptorProperties) {
        FactorySourceConnector.super.createConnector(sign, schema, descriptorProperties);
    }

    @Override
    default Map<String, String> requiredContext() {
        Map<String, String> context = new HashMap<>();
        context.put("connector.type", connectorType());
        Map<String, String> requiredConfig = requiredConfig();
        if (null != requiredConfig) {
            context.putAll(requiredConfig);
        }
        return context;
    }

    @Override
    default List<String> supportedProperties() {
        List<String> columns = Lists.newArrayList("schema.#.name", "schema.#.data-type", "schema.table");
        String[] strings = supportedConfig();
        if (null == strings || strings.length == 0) {
            return columns;
        }
        List<String> strings1 = Arrays.asList(strings);
        strings1.addAll(columns);
        return strings1;
    }
}

package com.chua.utils.netx.flink.connector;

import com.chua.utils.netx.flink.format.FormatConnector;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.descriptors.DescriptorProperties;

/**
 * redis table source sink
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/21
 */
public interface FactorySourceConnector {
    /**
     * 创建链接
     *
     * @param sign                 信号
     * @param schema               Schema
     * @param descriptorProperties 属性
     */
    default void createConnector(String sign, TableSchema schema, DescriptorProperties descriptorProperties) {
        FormatConnector.setDescriptorProperties(sign, descriptorProperties);
        FormatConnector.setSchema(sign, schema);
    }
}

package com.chua.utils.netx.flink.connector;

import org.apache.flink.table.descriptors.ConnectorDescriptorValidator;
import org.apache.flink.table.descriptors.DescriptorProperties;

/**
 * validator
 * @author CH
 * @version 1.0.0
 * @since 2021/1/21
 */
public class MemConnectorDescriptorValidator extends ConnectorDescriptorValidator {
    public static final String CONNECTOR_TYPE_VALUE = "mem";

    @Override
    public void validate(DescriptorProperties properties) {
        /**
         * 这里校验,比如配置中有address属性，是ip:port格式 可以对其进行校验
         */
        super.validate(properties);
        properties.validateValue(CONNECTOR_TYPE, CONNECTOR_TYPE_VALUE, false);
    }
}

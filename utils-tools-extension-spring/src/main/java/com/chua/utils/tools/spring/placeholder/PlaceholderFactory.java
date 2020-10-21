package com.chua.utils.tools.spring.placeholder;

import lombok.NoArgsConstructor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

import static org.springframework.beans.factory.config.PlaceholderConfigurerSupport.*;

/**
 * 占位符处理工厂
 * @author CH
 * @version 1.0.0
 * @since 2020/10/21
 */
@NoArgsConstructor
public class PlaceholderFactory {
    /**
     * 数据
     */
    private Properties properties;
    private PropertyPlaceholderHelper propertyPlaceholderHelper;
    /**
     * 前置
     */
    protected String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;
    /**
     * 后置
     */
    protected String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;
    /**
     * 数据分隔符
     */
    protected String valueSeparator = DEFAULT_VALUE_SEPARATOR;
    /**
     * 忽略不可解析的占位符
     */
    protected boolean ignoreUnresolvablePlaceholders = false;

    public PlaceholderFactory(final Properties properties) {
        this(DEFAULT_PLACEHOLDER_PREFIX, DEFAULT_PLACEHOLDER_SUFFIX, DEFAULT_VALUE_SEPARATOR, true, properties);
    }

    public PlaceholderFactory(String placeholderPrefix, String placeholderSuffix, Properties properties) {
        this(placeholderPrefix, placeholderSuffix, DEFAULT_VALUE_SEPARATOR, true, properties);
    }

    public PlaceholderFactory(String placeholderPrefix, String placeholderSuffix, String valueSeparator, boolean ignoreUnresolvablePlaceholders, Properties properties) {
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
        this.valueSeparator = valueSeparator;
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
        this.properties = properties;
        this.propertyPlaceholderHelper = new PropertyPlaceholderHelper(placeholderPrefix, placeholderSuffix, valueSeparator, ignoreUnresolvablePlaceholders);
    }

    /**
     * 解析字符串
     * @param value
     * @return
     */
    public String analyse(final String value) {
        return propertyPlaceholderHelper.replacePlaceholders(value, properties);
    }
}

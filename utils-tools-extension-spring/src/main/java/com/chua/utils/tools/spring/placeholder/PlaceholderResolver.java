package com.chua.utils.tools.spring.placeholder;

import lombok.NoArgsConstructor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.SystemPropertyUtils;

import java.util.Properties;

import static org.springframework.beans.factory.config.PlaceholderConfigurerSupport.*;

/**
 * 占位符处理工厂
 * @author CH
 * @version 1.0.0
 * @since 2020/10/21
 */
public class PlaceholderResolver {

    private Iterable<PropertySource<?>> sources;

    private PropertyPlaceholderHelper helper;

    public PlaceholderResolver(Environment environment) {
        this.sources = getSources(environment);
        this.helper = (helper != null) ? helper : new PropertyPlaceholderHelper(SystemPropertyUtils.PLACEHOLDER_PREFIX,
                SystemPropertyUtils.PLACEHOLDER_SUFFIX, SystemPropertyUtils.VALUE_SEPARATOR, true);
    }

    /**
     * 解决占位符
     *
     * @param value 值
     * @return
     */
    public Object resolvePlaceholders(Object value) {
        if (value instanceof String) {
            return this.helper.replacePlaceholders((String) value, this::resolvePlaceholder);
        }
        return value;
    }

    /**
     * 解决占位符
     * @param placeholder 占位符
     * @return
     */
    protected String resolvePlaceholder(String placeholder) {
        if (this.sources != null) {
            for (PropertySource<?> source : this.sources) {
                Object value = source.getProperty(placeholder);
                if (value != null) {
                    return String.valueOf(value);
                }
            }
        }
        return null;
    }

    private static PropertySources getSources(Environment environment) {
        Assert.notNull(environment, "Environment must not be null");
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment,
                "Environment must be a ConfigurableEnvironment");
        return ((ConfigurableEnvironment) environment).getPropertySources();
    }

}

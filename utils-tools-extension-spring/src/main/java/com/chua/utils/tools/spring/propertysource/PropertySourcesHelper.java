package com.chua.utils.tools.spring.propertysource;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.ObjectHelper;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.Map;
import java.util.Properties;

/**
 * PropertySources工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/5/9 15:27
 */
public class PropertySourcesHelper {
    /**
     * 数据转化
     *
     * @param propertySource
     * @return
     */
    public static Properties convert(final PropertySource propertySource) {
        return convert(propertySource, null);
    }

    /**
     * 数据转化
     *
     * @param propertySource
     * @param environment
     * @return
     */
    public static Properties convert(final PropertySource propertySource, final Environment environment) {
        if (null == propertySource) {
            return null;
        }

        Properties properties = new Properties();
        Object source = propertySource.getSource();
        if (source instanceof Map) {
            Object value;
            for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) source).entrySet()) {
                value = entry.getValue();
                if (ClassHelper.isAssignableFrom(value, "org.springframework.boot.origin.OriginTrackedValue")) {
                    value = ClassHelper.getFieldByName(value, "value");
                }
                properties.put(entry.getKey(), ObjectHelper.defaultIfNull(value, "").toString());
            }
        }

        return properties;
    }
}

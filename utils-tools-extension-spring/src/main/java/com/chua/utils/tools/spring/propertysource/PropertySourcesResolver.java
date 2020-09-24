package com.chua.utils.tools.spring.propertysource;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.*;
import java.util.function.Consumer;

/**
 * PropertySources工具
 * @author CH
 */
public class PropertySourcesResolver {

    private MutablePropertySources propertySources;
    private Environment environment;
    private List<Properties> properties = new ArrayList<>();

    public PropertySourcesResolver(MutablePropertySources propertySources) {
        this.propertySources = propertySources;
        this.convertProperty();
    }

    public PropertySourcesResolver(Environment environment) {
        this.environment = environment;
        this.propertySources = null != environment && environment instanceof ConfigurableEnvironment ? ((ConfigurableEnvironment) environment).getPropertySources(): null;
        this.convertProperty();
    }

    public List<Properties> properties() {
        return properties;
    }

    /**
     *
     */
    private void convertProperty() {
        if (null == propertySources) {
            return;
        }
        propertySources.stream().forEach(new Consumer<PropertySource<?>>() {
            @Override
            public void accept(PropertySource<?> propertySource) {
                if("random".equals(propertySource.getName()) || "systemProperties".equals(propertySource.getName())) {
                    return;
                }
                Properties convert = PropertySourcesHelper.convert(propertySource, environment);
                properties.add(convert);
            }
        });
    }
    /**
     * 删除属性
     * @param keyName 索引
     */
    public PropertySource<?> propertySource(String keyName) throws UnsupportedOperationException {
        Iterator<PropertySource<?>> iterator = propertySources.iterator();
        for (PropertySource<?> propertySource : propertySources) {
            PropertySource<?> next = iterator.next();
            Object source = next.getSource();
            if (source instanceof Map) {
                if(((Map) source).containsKey(keyName)) {
                    return (PropertySource<?>) next;
                }
            }
        }
        return null;
    }
    /**
     * 删除属性
     * @param keyName 索引
     */
    public void remove(String keyName) throws UnsupportedOperationException {
        PropertySource<?> propertySource = propertySource(keyName);
        if(null == propertySource) {
            return;
        }
        Object source = propertySource.getSource();
        if(source instanceof Map) {
            Map<Object, Object> param = (Map<Object, Object>) source;
            param.remove(keyName);
        }
    }
    /**
     * 删除属性
     * @param index 索引
     * @param propName 属性名称
     */
    public void remove(String index, final String propName) throws UnsupportedOperationException {
        PropertySource<?> propertySource = propertySources.get(propName);
        if(propertySource.containsProperty(index)) {
            Object source = propertySource.getSource();
            if(source instanceof Map) {
                Map<Object, Object> param = (Map<Object, Object>) source;
                param.remove(index);
            }
        }
    }
}

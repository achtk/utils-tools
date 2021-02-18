package com.chua.utils.tools.collects;

import com.chua.utils.tools.prop.placeholder.EnvPropertyPropertyPlaceholder;
import com.chua.utils.tools.prop.placeholder.PropertiesPropertyPlaceholder;
import com.chua.utils.tools.prop.placeholder.PropertyPlaceholder;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基础配置
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/7
 */
@EqualsAndHashCode
final class BasicProfile implements Profile {

    private final Map<String, Properties> propertiesMap = new LinkedHashMap<>();
    private final Set<PropertyPlaceholder> holders = new HashSet<>();

    {
        holders.add(new PropertiesPropertyPlaceholder());
        holders.add(new EnvPropertyPropertyPlaceholder());
    }

    @Override
    public Properties getProfile(String name) {
        return propertiesMap.get(name);
    }

    @Override
    public Object getValue(String name, String key) {
        if (null == key) {
            return null;
        }

        Properties properties = getProfile(name);
        if (null == properties) {
            return null;
        }
        return handler(properties.get(key));
    }

    @Override
    public Object getValue(String key) {
        if (null == key) {
            return null;
        }
        Object value = null;
        for (Properties properties : propertiesMap.values()) {
            if (properties.containsKey(key)) {
                value = properties.get(key);
            }
        }
        return handler(value);
    }

    @Override
    public List<Object> getValues(String key) {
        if (null == key) {
            return null;
        }
        List<Object> value = new ArrayList<>();
        for (Properties properties : propertiesMap.values()) {
            if (properties.containsKey(key)) {
                value.add(properties.get(key));
            }
        }
        return value.stream().map(item -> handler(item)).collect(Collectors.toList());
    }

    @Override
    public Object placeholder(String key) {
        Object value = getValue(key);
        return handler(value);
    }

    @Override
    public Object handler(Object value) {
        Object result = value;
        for (PropertyPlaceholder placeholder : holders) {
            placeholder.addPropertySource(propertiesMap);
            Object tempResult = placeholder.placeholder(value);
            if (null != tempResult) {
                result = tempResult;
            }
        }
        return result;
    }

    @Override
    public void propertyPlaceholder(PropertyPlaceholder propertyPlaceholder) {
        if (null == propertyPlaceholder) {
            return;
        }
        holders.add(propertyPlaceholder);
    }

    @Override
    public void set(String name, String key, Object value) {
        synchronized (propertiesMap) {
            if (propertiesMap.containsKey(name)) {
                Properties properties = propertiesMap.get(name);
                properties.put(key, value);
                return;
            }
            Properties properties = new Properties();
            properties.put(key, value);
            propertiesMap.put(name, properties);
        }
    }

    @Override
    public void set(String name, Properties properties) {
        synchronized (propertiesMap) {
            if (propertiesMap.containsKey(name)) {
                Properties properties1 = propertiesMap.get(name);
                properties1.putAll(properties);
                return;
            }
            propertiesMap.put(name, properties);
        }
    }

    @Override
    public void set(Profile profile) {
        Map<String, Properties> profiles = profile.profiles();
        for (Map.Entry<String, Properties> entry : profiles.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Map<String, Properties> profiles() {
        return propertiesMap;
    }
}

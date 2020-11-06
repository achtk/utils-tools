package com.chua.utils.tools.environment;

import com.chua.utils.tools.prop.source.PropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 标准环境
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/4
 */
public class StandardEnvironment implements Environment {

    private final CopyOnWriteArrayList<PropertySource> copyOnWriteArrayList = new CopyOnWriteArrayList<>();

    @Override
    public void addPropertySource(PropertySource propertySource) {
        copyOnWriteArrayList.add(propertySource);
    }

    @Override
    public void addLastPropertySource(PropertySource propertySource) {
        copyOnWriteArrayList.add(copyOnWriteArrayList.size(), propertySource);
    }

    @Override
    public void addFirstPropertySource(PropertySource propertySource) {
        copyOnWriteArrayList.add(0, propertySource);
    }

    @Override
    public PropertySource get(String name) {
        for (PropertySource propertySource : copyOnWriteArrayList) {
            if (!propertySource.getName().equals(name)) {
                continue;
            }
            return propertySource;
        }
        return null;
    }

    @Override
    public Object getObject(String name) {
        Object value = null;
        for (PropertySource propertySource : copyOnWriteArrayList) {
            Map<String, Object> source = propertySource.getSource();
            if (source.containsKey(name)) {
                value = source.get(name);
            }
        }
        return value;
    }

    @Override
    public List<Object> getList(String name) {
        List<Object> value = new ArrayList<>();
        for (PropertySource propertySource : copyOnWriteArrayList) {
            Map<String, Object> source = propertySource.getSource();
            if (source.containsKey(name)) {
                value.add(source.get(name));
            }
        }
        return value;
    }
}

package com.chua.utils.tools.spring.environment;

import com.chua.utils.tools.common.*;
import com.chua.utils.tools.proxy.JavassistProxyAgent;
import com.chua.utils.tools.proxy.ProxyAgent;
import com.chua.utils.tools.spring.propertysource.PropertySourcesResolver;
import com.google.common.base.Strings;
import net.sf.cglib.beans.BeanMap;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import static com.chua.utils.tools.constant.StringConstant.*;

/**
 * 环境变量读取
 * @author CH
 * @version 1.0.0
 * @since 2020/3/24 15:37
 */
public class EnvironmentFactory {

    private Environment environment;

    public EnvironmentFactory(ApplicationContext applicationContext) {
        this.environment = applicationContext == null ? null : applicationContext.getEnvironment();
    }

    public EnvironmentFactory(ConfigurableListableBeanFactory beanFactory) {
        this.environment = beanFactory == null ? null : beanFactory.getBean(Environment.class);
    }

    public EnvironmentFactory(Environment environment) {
        this.environment = environment;
    }


    /**
     * 将配置渲染到bean
     * @param tClass
     * @param <T>
     * @return
     */
    public <T>T getBean(Class<T> tClass) {
        return getBean(tClass, "");
    }
    /**
     * 将配置渲染到bean
     * @param tClass
     * @param <T>
     * @return
     */
    public <T>T getBean(Class<T> tClass, final String prefix) {
        if (null == tClass) {
            return null;
        }

        T t = null;
        try {
            t = tClass.newInstance();
        } catch (Throwable e) {
        }
        Field[] fields = tClass.getDeclaredFields();
        Map<String, Object> params = new HashMap<>();
        String name, property;
        for (Field field : fields) {
            name = field.getName();
            property = environment.getProperty(prefix + "." + name);
            if(null == property) {
                property = environment.getProperty(prefix + "." + StringHelper.humpToMin2(name));
            }
            if(null == t || null == property) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.set(t, property);
            } catch (IllegalAccessException e) {
                continue;
            }
        }
        return t;
    }

    /**
     * 获取变量值
     * @param key 索引
     * @return
     */
    public String getProperty(final String key) {
        return environment.getProperty(key);
    }
    /**
     * 获取变量值
     * @param key1 索引
     * @param keys 索引
     * @return
     */
    public String getProperty(final String key1, String... keys) {
        String property = environment.getProperty(key1);
        if(null != property) {
            return property;
        }

        if(!BooleanHelper.hasLength(keys)) {
            return null;
        }

        for (String key : keys) {
            property = environment.getProperty(key);
            if(!Strings.isNullOrEmpty(property)) {
                break;
            }
        }

        return property;
    }

    /**
     * 渲染对象
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T> T inject(Class<T> entityClass) {
        if(null == entityClass) {
            return null;
        }

        T t = null;
        if(entityClass.isInterface() || Modifier.isAbstract(entityClass.getModifiers())) {
            ProxyAgent proxy = new JavassistProxyAgent(entityClass);
            t = (T) proxy.newProxy();
        } else {
            try {
                t = entityClass.newInstance();
            } catch (Throwable e) {
                return null;
            }
        }

        if(null == t) {
            return null;
        }

        Map<String, Object> propertyFactory = getPropertyFactory();
        BeanMap beanMap = BeanMap.create(t);
        Field[] fields = entityClass.getDeclaredFields();
        for (Object o : beanMap.keySet()) {
            renderBeanMap(o.toString(), beanMap, propertyFactory);
        }
        return (T) beanMap.getBean();
    }

    /**
     * 渲染beanmap
     * @param name
     * @param beanMap
     * @param params
     */
    private void renderBeanMap(String name, BeanMap beanMap, Map<String, Object> params) {

        if(!BooleanHelper.hasLength(params)) {
            return;
        }
        Object element = FinderHelper.firstElement(params);
        if(null == element) {
            return;
        }

        String type = beanMap.getPropertyType(name).getName().toLowerCase();
        if(CLASS_INT.equals(type)) {
            beanMap.put(name, NumberHelper.toInt(element.toString(), 0));
        } else if(CLASS_LONG.equals(type)) {
            beanMap.put(name, NumberHelper.toLong(element.toString(), 0L));
        } else if(CLASS_DOUBLE.equals(type)) {
            beanMap.put(name, NumberHelper.toDouble(element.toString(), 0D));
        } else if(CLASS_BOOLEAN.equals(type)) {
            beanMap.put(name, BooleanHelper.toBoolean(element.toString()));
        } else if(CLASS_CHAR.equals(type)) {
            beanMap.put(name, (char)element);
        } else if(CLASS_SHORT.equals(type)) {
            beanMap.put(name, NumberHelper.toShort(element.toString(), (short) 0));
        } else if(CLASS_FLOAT.equals(type)) {
            beanMap.put(name, NumberHelper.toFloat(element.toString(), 0f));
        } else if(CLASS_BYTE.equals(type)) {
            beanMap.put(name, NumberHelper.toByte(element.toString(), (byte) 0));
        }
        beanMap.put(name, element);

    }

    /**
     * 获取变量值
     * @param key 索引
     * @param defaultValue 默认值
     * @return
     */
    public String getProperty(final String key, final String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    /**
     * 获取变量值
     * @param key 索引
     * @param tClass 类型
     * @param <T>
     * @return
     */
    public <T>T getProperty(final String key, final Class<T> tClass) {
        return environment.getProperty(key, tClass);
    }
    /**
     * 获取变量值
     * @param key 索引
     * @param tClass 类型
     * @param <T>
     * @param defaultValue 默认值
     * @return
     */
    public <T>T getProperty(final String key, final Class<T> tClass, final T defaultValue) {
        return environment.getProperty(key, tClass, defaultValue);
    }
    /**
     * 获取激活的profiles
     * @return
     */
    public String[] getActiveProfiles() {
        return environment.getActiveProfiles();
    }

    /**
     * 获取默认的profiles
     * @return
     */
    public String[] getDefaultProfiles() {
        return environment.getDefaultProfiles();
    }
    /**
     * 获取默认的profiles
     * @return
     */
    public boolean acceptsProfiles(final String profile) {
        return environment.acceptsProfiles(profile);
    }

    /**
     * 是否包含索引
     * @param key 索引
     * @return
     */
    public boolean containsProperty(final String key) {
        return environment.containsProperty(key);
    }

    /**
     * 处理占位符
     * @param key 索引
     * @return
     */
    public String resolvePlaceholders(final String key) {
        return environment.resolvePlaceholders(key);
    }

    /**
     * 获取PropertySources
     * @return
     */
    public MutablePropertySources getPropertySources() {
        if(environment instanceof StandardEnvironment) {
            StandardEnvironment standardEnvironment = (StandardEnvironment) environment;
            return standardEnvironment.getPropertySources();
        }
        return null;
    }
    /**
     * 获取PropertySources
     * @return
     */
    public Map<String, Object> getPropertyFactory() {
        Map<String, Object> params = new HashMap<>();
        MutablePropertySources propertySources = getPropertySources();
        if(null == propertySources) {
            return params;
        }

        Iterator<PropertySource<?>> iterator = propertySources.iterator();
        for (PropertySource<?> propertySource : propertySources) {
            Object source = propertySource.getSource();
            if(source instanceof Map) {
                params.putAll((Map<String, Object>) source);
            }
        }

        return params;
    }
    /**
     * 添加PropertySources
     * @return
     */
    public void addLastPropertySources(final String name, final Properties properties) {
        MutablePropertySources propertySources = getPropertySources();
        if(null == propertySources) {
            return;
        }
        PropertySource propertySource = new PropertiesPropertySource(name, properties);
        propertySources.addLast(propertySource);
    }

    /**
     * 添加PropertySources
     * @return
     */
    public void addFirstPropertySources(final String name, final Properties properties) {
        MutablePropertySources propertySources = getPropertySources();
        if(null == propertySources) {
            return;
        }
        PropertySource propertySource = new PropertiesPropertySource(name, properties);
        propertySources.addFirst(propertySource);
    }
    /**
     * 删除PropertySources
     * @return
     */
    public void removePropertySources(final String name) {
        MutablePropertySources propertySources = getPropertySources();
        if(null == propertySources) {
            return;
        }
        propertySources.remove(name);
    }
    /**
     * 替换PropertySources
     * @return
     */
    public void replacePropertySources(final String name, final Properties properties) {
        MutablePropertySources propertySources = getPropertySources();
        if(null == propertySources) {
            return;
        }
        PropertySource propertySource = new PropertiesPropertySource(name, properties);
        propertySources.replace(name, propertySource);
    }
    /**
     * 获取默认的profiles
     * @return
     */
    public Map<String, Object> maps(final String prefix, final boolean simpleName) {
        if(StringHelper.isBlank(prefix)) {
            return null;
        }
        MutablePropertySources propertySources = getPropertySources();
        if(null == propertySources) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        propertySources.stream().forEach(new Consumer<PropertySource<?>>() {
            @Override
            public void accept(PropertySource<?> propertySource) {
                Object source = propertySource.getSource();
                if(!(source instanceof Map)) {
                    return;
                }
                filterMap(map, (Map)source, prefix, simpleName);
            }
        });

        return map;
    }

    /**
     * 过滤map
     * @param map 结果集
     * @param source 数据集
     * @param prefix 前缀
     * @param simpleName 是否为简单名称
     */
    private void filterMap(Map<String, Object> map, Map<String, Object> source, String prefix, boolean simpleName) {
        if(null == source) {
            return;
        }

        String key = "";
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            key = entry.getKey();
            if(FileHelper.wildcardMatch(key, prefix)) {
                if(simpleName) {
                    map.put(FileHelper.getExtension(key), entry.getValue());
                } else {
                    map.put(key, entry.getValue());
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public Properties properties() {
        if(environment instanceof ConfigurableEnvironment) {
            PropertySourcesResolver propertySourcesResolver = new PropertySourcesResolver(environment);
            return FinderHelper.firstElement(propertySourcesResolver.properties());
        }
        return null;
    }
}

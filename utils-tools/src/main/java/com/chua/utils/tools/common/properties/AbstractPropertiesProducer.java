package com.chua.utils.tools.common.properties;

import com.chua.utils.tools.cfg.CfgConfig;
import com.chua.utils.tools.cfg.CfgOptions;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.constant.NumberConstant;
import com.chua.utils.tools.text.IdHelper;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;

/**
 * properties信息提供者
 *
 * @author CH
 * @date 2020-09-29
 */
public abstract class AbstractPropertiesProducer {

    private ConcurrentHashMap<String, Properties> source = new ConcurrentHashMap<>();
    private static final PropertiesConfiguration DEFAULT_PROPERTIES_CONFIGURATION = new PropertiesConfiguration();
    private PropertiesConfiguration configuration = DEFAULT_PROPERTIES_CONFIGURATION;
    private static final Properties DEFAULT_PROPERTIES = new Properties();

    public AbstractPropertiesProducer() {
    }

    /**
     * 初始化
     *
     * @param source        数据源
     * @param configuration 配置
     */
    public AbstractPropertiesProducer(Map<String, Properties> source, PropertiesConfiguration configuration) {
        if (null != source) {
            this.source.putAll(source);
        }
        this.configuration = null == configuration ? DEFAULT_PROPERTIES_CONFIGURATION : configuration;
    }

    /**
     * 删除数据
     *
     * @param name 名称
     * @return
     */
    public synchronized AbstractPropertiesProducer remove(final String name) {
        if (null != name) {
            this.source.remove(name);
        }
        return this;
    }

    /**
     * 添加数据
     *
     * @param name       名称
     * @param properties 数据
     * @return
     */
    public AbstractPropertiesProducer put(final String name, final Properties properties) {
        if (null != properties) {
            this.source.put(null == name ? UUID.randomUUID().toString() : name, properties);
        }
        return this;
    }

    /**
     * 添加数据
     *
     * @param properties 数据
     * @return
     */
    public AbstractPropertiesProducer put(final Properties properties) {
        return put(null, properties);
    }

    /**
     * 获取 set
     *
     * @param valueClass 类型
     * @param keys       索引
     * @return
     */
    public <V> Map<String, V> mapStrings(final Class<V> valueClass, final String... keys) {
        return maps(null, String.class, valueClass, keys);
    }

    /**
     * 获取 set
     *
     * @param keys 索引
     * @return
     */
    public Map<String, Object> mapStringObjects(final String... keys) {
        return maps(null, String.class, Object.class, keys);
    }

    /**
     * 获取 set
     *
     * @param keys 索引
     * @return
     */
    public Map<String, Object> mapDefaultStringObjects(final String... keys) {
        return maps(Collections.emptyMap(), String.class, Object.class, keys);
    }

    /**
     * 获取 set
     *
     * @param keys 索引
     * @return
     */
    public Map<String, String> mapDefaultStringStrings(final String... keys) {
        return maps(Collections.emptyMap(), String.class, String.class, keys);
    }

    /**
     * 获取 set
     *
     * @param keys 索引
     * @return
     */
    public Map<String, String> mapStringStrings(final String... keys) {
        return maps(null, String.class, String.class, keys);
    }

    /**
     * 获取 set
     *
     * @param keyClass   类型
     * @param valueClass 类型
     * @param keys       索引
     * @return
     */
    public <K, V> Map<K, V> maps(final Class<K> keyClass, final Class<V> valueClass, final String... keys) {
        return maps(null, keyClass, valueClass, keys);
    }

    /**
     * 获取 map
     *
     * @param defaultValue 默认值
     * @param keyClass     类型
     * @param valueClass   类型
     * @param keys         索引
     * @return
     */
    public <K, V> Map<K, V> mapIgnore(final Map<K, V> defaultValue, final Class<K> keyClass, final Class<V> valueClass, final String... keys) {
        Object objects = objects(defaultValue, keys);
        return objects instanceof Map ? (Map<K, V>) objects : defaultValue;
    }

    /**
     * 获取 map
     *
     * @param keyClass   类型
     * @param valueClass 类型
     * @param keys       索引
     * @return
     */
    public <K, V> Map<K, V> mapIgnore(final Class<K> keyClass, final Class<V> valueClass, final String... keys) {
        Object objects = objects(Collections.emptyMap(), keys);
        return objects instanceof Map ? (Map<K, V>) objects : Collections.emptyMap();
    }

    /**
     * 获取 map
     *
     * @param valueClass 类型
     * @param keys       索引
     * @return
     */
    public <V> Map<String, V> mapStringIgnore(final Class<V> valueClass, final String... keys) {
        Object objects = objects(Collections.emptyMap(), keys);
        return objects instanceof Map ? (Map<String, V>) objects : Collections.emptyMap();
    }

    /**
     * 获取 map
     *
     * @param keys 索引
     * @return
     */
    public Map<String, Object> mapIgnore(final String... keys) {
        Object objects = objects(Collections.emptyMap(), keys);
        return objects instanceof Map ? (Map<String, Object>) objects : Collections.emptyMap();
    }

    /**
     * 获取 map
     *
     * @param defaultValue 默认值
     * @param keyClass     类型
     * @param valueClass   类型
     * @param keys         索引
     * @return
     */
    public <K, V> Map<K, V> maps(final Map<K, V> defaultValue, final Class<K> keyClass, final Class<V> valueClass, final String... keys) {
        Object objects = objects(defaultValue, keys);

        if (null == objects) {
            return isNeedEmptyCollection(Collections.emptyMap());
        }
        Map<K, V> result = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        if (objects instanceof Map) {
            Map value = (Map) objects;
            if (value.size() == 0 || null == keyClass || null == valueClass) {
                return isNeedEmptyCollection(Collections.emptyMap());
            }
            for (Object o : value.keySet()) {
                if (null == o) {
                    continue;
                }
                if (!keyClass.isAssignableFrom(o.getClass())) {
                    continue;
                }
                Object o1 = value.get(o);
                if (!valueClass.isAssignableFrom(o1.getClass())) {
                    continue;
                }
                result.put((K) o, (V) o1);
            }
            return result;
        }
        return isNeedEmptyCollection(Collections.emptyMap());
    }

    /**
     * 获取 set
     *
     * @param keys 索引
     * @return
     */
    public Set<Object> sets(final String... keys) {
        return sets(null, Object.class, keys);
    }

    /**
     * 获取 set
     *
     * @param keys 索引
     * @return
     */
    public Set<Object> setDefaults(final String... keys) {
        return sets(Collections.emptySet(), Object.class, keys);
    }

    /**
     * 获取 set
     *
     * @param tClass 类型
     * @param keys   索引
     * @return
     */
    public <T> Set<T> sets(final Class<T> tClass, final String... keys) {
        return sets(null, tClass, keys);
    }

    /**
     * 获取 set
     *
     * @param defaultValue 默认值
     * @param tClass       类型
     * @param keys         索引
     * @return
     */
    public <T> Set<T> sets(final Set<T> defaultValue, final Class<T> tClass, final String... keys) {
        Object objects = objects(defaultValue, keys);

        if (null == objects) {
            return isNeedEmptyCollection(Collections.emptySet());
        }
        Set<T> result = new HashSet<>();
        if (objects instanceof Set) {
            Set value = (Set) objects;
            if (value.size() == 0 || null == tClass) {
                return isNeedEmptyCollection(Collections.emptySet());
            }

            for (Object o : value) {
                if (whetherToIgnoreWhenNull(o)) {
                    result.add(null);
                    continue;
                }
                if (tClass.isAssignableFrom(o.getClass())) {
                    result.add((T) o);
                    continue;
                }
            }
            return result;
        }
        //强制转化
        if (configuration.isTypeInconsistentForcedConversion()) {
            if (null != objects && !tClass.isAssignableFrom(objects.getClass())) {
                return isNeedEmptyCollection(Collections.emptySet());
            }
            result.add((T) objects);
            return result;
        }
        return isNeedEmptyCollection(Collections.emptySet());
    }

    /**
     * 获取 list
     *
     * @param keys 索引
     * @return
     */
    public List<Object> listDefaults(final String... keys) {
        return lists(Collections.emptyList(), Object.class, keys);
    }

    /**
     * 获取 list
     *
     * @param tClass 类型
     * @param keys   索引
     * @return
     */
    public <T> List<T> listDefaults(final Class<T> tClass, final String... keys) {
        return lists(Collections.emptyList(), tClass, keys);
    }

    /**
     * 获取 list
     *
     * @param tClass 类型
     * @param keys   索引
     * @return
     */
    public <T> List<T> lists(final Class<T> tClass, final String... keys) {
        return lists(null, tClass, keys);
    }

    /**
     * 获取 list
     *
     * @param keys 索引
     * @return
     */
    public List<Object> lists(final String... keys) {
        return lists(null, Object.class, keys);
    }

    /**
     * 获取 list
     *
     * @param keys 索引
     * @return
     */
    public List<String> listStrings(final String... keys) {
        return lists(null, String.class, keys);
    }

    /**
     * 获取 list
     *
     * @param defaultValue 默认值
     * @param tClass       类型
     * @param keys         索引
     * @return
     */
    public <T> List<T> listIgnore(final List<T> defaultValue, final Class<T> tClass, final String... keys) {
        Object objects = objects(defaultValue, keys);
        return objects instanceof List ? (List<T>) objects : Collections.emptyList();
    }

    /**
     * 获取 list
     *
     * @param tClass 类型
     * @param keys   索引
     * @return
     */
    public <T> List<T> listIgnore(final Class<T> tClass, final String... keys) {
        Object objects = objects(null, keys);
        return objects instanceof List ? (List<T>) objects : null;
    }

    /**
     * 获取 list
     *
     * @param keys 索引
     * @return
     */
    public List<String> listIgnore(final String... keys) {
        Object objects = objects(null, keys);
        return objects instanceof List ? (List<String>) objects : null;
    }

    /**
     * 获取 list
     *
     * @param defaultValue 默认值
     * @param tClass       类型
     * @param keys         索引
     * @return
     */
    public <T> List<T> lists(final List<T> defaultValue, final Class<T> tClass, final String... keys) {
        Object objects = objects(defaultValue, keys);

        if (null == objects) {
            return isNeedEmptyCollection(Collections.emptyList());
        }
        List<T> result = new ArrayList<>();
        if (objects instanceof List) {
            List value = (List) objects;
            if (value.size() == 0 || null == tClass) {
                return isNeedEmptyCollection(Collections.emptyList());
            }

            for (Object o : value) {
                if (whetherToIgnoreWhenNull(o)) {
                    result.add(null);
                    continue;
                }
                if (tClass.isAssignableFrom(o.getClass())) {
                    result.add((T) o);
                    continue;
                }
            }
            return result;
        }
        //强制转化
        if (configuration.isTypeInconsistentForcedConversion()) {
            if (null != objects && !tClass.isAssignableFrom(objects.getClass())) {
                return isNeedEmptyCollection(Collections.emptyList());
            }
            result.add((T) objects);
            return result;
        }
        return isNeedEmptyCollection(Collections.emptyList());
    }

    /**
     * 获取字符串
     *
     * @param keys 索引
     */
    public String strings(final String... keys) {
        return strings(null, keys);
    }

    /**
     * 获取字符串
     *
     * @param defaultValue 默认值
     * @param keys         索引
     */
    public String strings(final String defaultValue, final String... keys) {
        Object objects = objects(defaultValue, keys);

        if (null == objects) {
            return null;
        }
        if (objects instanceof String) {
            return (String) objects;
        }
        //强制转化
        return configuration.isTypeInconsistentForcedConversion() ? objects.toString() : null;
    }

    /**
     * 获取 integer
     *
     * @param keys 索引
     */
    public Integer ints(final String... keys) {
        return ints(0, keys);
    }

    /**
     * 获取 integer
     *
     * @param defaultValue 默认值
     * @param keys         索引
     */
    public Integer ints(final Integer defaultValue, final String... keys) {
        Object objects = objects(defaultValue, keys);

        if (null == objects) {
            return null;
        }
        if (objects instanceof Integer) {
            return (Integer) objects;
        }
        //强制转化
        if (configuration.isTypeInconsistentForcedConversion()) {
            try {
                return Integer.valueOf(objects.toString());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * 获取 Long
     *
     * @param keys 索引
     */
    public Long longs(final String... keys) {
        return longs(0L, keys);
    }

    /**
     * 获取 Long
     *
     * @param defaultValue 默认值
     * @param keys         索引
     */
    public Long longs(final Long defaultValue, final String... keys) {
        Object objects = objects(defaultValue, keys);

        if (null == objects) {
            return null;
        }
        if (objects instanceof Long) {
            return (Long) objects;
        }
        //强制转化
        if (configuration.isTypeInconsistentForcedConversion()) {
            try {
                return Long.valueOf(objects.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取 Double
     *
     * @param keys 索引
     */
    public Double doubles(final String... keys) {
        return doubles(0D, keys);
    }

    /**
     * 获取 Double
     *
     * @param defaultValue 默认值
     * @param keys         索引
     */
    public Double doubles(final Double defaultValue, final String... keys) {
        Object objects = objects(defaultValue, keys);

        if (null == objects) {
            return null;
        }
        if (objects instanceof Double) {
            return (Double) objects;
        }
        //强制转化
        if (configuration.isTypeInconsistentForcedConversion()) {
            try {
                return Double.valueOf(objects.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取 Float
     *
     * @param keys 索引
     */
    public Float floats(final String... keys) {
        return floats(0f, keys);
    }

    /**
     * 获取 Float
     *
     * @param defaultValue 默认值
     * @param keys         索引
     */
    public Float floats(final Float defaultValue, final String... keys) {
        Object objects = objects(defaultValue, keys);

        if (null == objects) {
            return null;
        }
        if (objects instanceof Float) {
            return (Float) objects;
        }
        //强制转化
        if (configuration.isTypeInconsistentForcedConversion()) {
            try {
                return Float.valueOf(objects.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取 Short
     *
     * @param keys 索引
     */
    public Short shorts(final String... keys) {
        return shorts((short) 0, keys);
    }

    /**
     * 获取 Short
     *
     * @param defaultValue 默认值
     * @param keys         索引
     */
    public Short shorts(final Short defaultValue, final String... keys) {
        Object objects = objects(defaultValue, keys);

        if (null == objects) {
            return null;
        }
        if (objects instanceof Short) {
            return (Short) objects;
        }
        //强制转化
        if (configuration.isTypeInconsistentForcedConversion()) {
            try {
                return Short.valueOf(objects.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取对象
     *
     * @param defaultValue 默认值
     * @param keys         索引
     */
    public Object objects(final Object defaultValue, final String... keys) {
        if (null == keys || keys.length == 0 || null == source || source.isEmpty()) {
            return defaultValue;
        }

        Object newValue = null;
        for (Properties properties : source.values()) {
            Object value = getValue(properties, keys);
            if (null == value) {
                continue;
            }
            newValue = value;
        }
        return null == newValue ? defaultValue : newValue;
    }

    /**
     * 匹配并替換
     *
     * @param key      索引
     * @param newValue 新值
     */
    public void wildcardMatchAndReplace(final String key, final Object newValue) {
        Properties properties = wildcardMatch(key);
        if (!BooleanHelper.hasLength(properties)) {
            return;
        }
        for (Map.Entry<Object, Object> objectEntry : properties.entrySet()) {
            for (Map.Entry<String, Properties> entry : source.entrySet()) {
                if (!entry.getValue().containsKey(objectEntry.getKey())) {
                    continue;
                }
                entry.getValue().put(objectEntry.getKey(), newValue);
            }
        }
    }

    /**
     * 获取对象
     *
     * @param keys 索引
     */
    public Properties wildcardMatch(final String... keys) {
        if (null == keys || keys.length == 0 || null == source || source.isEmpty()) {
            return DEFAULT_PROPERTIES;
        }

        Properties result = new Properties();
        for (Properties properties : source.values()) {
            Properties fuzzyValue = getFuzzyValue(properties, keys);
            if (fuzzyValue.isEmpty()) {
                continue;
            }
            result.putAll(fuzzyValue);
        }
        return result;
    }

    /**
     * 获取值
     *
     * @param properties 数据
     * @param keys       索引
     * @return
     */
    protected Object getValue(Properties properties, String[] keys) {
        if (null == keys || keys.length == 0) {
            return null;
        }

        for (String key : keys) {
            Set<String> newKey = containsKeyByConfiguration(properties, key);
            if (null == newKey) {
                continue;
            }
            return properties.get(FinderHelper.firstElement(newKey));
        }
        return null;
    }

    /**
     * 获取值
     *
     * @param properties 数据
     * @param keys       索引
     * @return
     */
    protected Properties getFuzzyValue(Properties properties, String[] keys) {
        if (null == keys || keys.length == 0) {
            return DEFAULT_PROPERTIES;
        }

        Properties result = new Properties();
        for (String key : keys) {
            Set<String> newKey = containsKeyByConfiguration(properties, key);
            if (!BooleanHelper.hasLength(newKey)) {
                continue;
            }
            for (String s : newKey) {
                result.put(s, properties.getProperty(s));
            }
        }
        return result;
    }

    /**
     * 是否包含索引
     *
     * @param properties
     * @param key
     * @return
     */
    protected Set<String> containsKeyByConfiguration(Properties properties, String key) {
        if (!configuration.isAllowFuzzyMatching()) {
            return properties.containsKey(key) ? Sets.newHashSet(key) : null;
        }
        Set<String> result = Sets.newHashSet();
        for (Object o : properties.keySet()) {
            if (!StringHelper.wildcardMatch(o.toString(), key)) {
                continue;
            }
            result.add(o.toString());
        }
        return result;
    }

    /**
     * 获取默认值
     *
     * @param emptyList
     * @param <T>
     * @return
     */
    protected <T> T isNeedEmptyCollection(T emptyList) {
        return configuration.isNeedEmptyCollection() ? emptyList : null;
    }

    /**
     * 当null时是否忽略
     *
     * @param <T>
     * @param source
     * @return
     */
    protected <T> boolean whetherToIgnoreWhenNull(T source) {
        return null == source && configuration.isCollectionIgnoresNull();
    }

    /**
     * 获取所有Map
     *
     * @return
     */
    protected ConcurrentHashMap<String, Properties> allMap() {
        return source;
    }

    public void cfgLoader(CfgConfig cfgConfig) {
        CfgOptions cfgOptions = new CfgOptions(cfgConfig);
        ConcurrentHashMap<String, Object> initialCfg = cfgOptions.toConcurrentHashMap();
        Properties properties = MapOperableHelper.toProp(initialCfg);
        this.put(IdHelper.createUuid(), properties);
    }
}

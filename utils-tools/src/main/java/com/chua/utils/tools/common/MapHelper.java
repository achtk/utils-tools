package com.chua.utils.tools.common;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.function.MethodIntercept;
import com.google.common.collect.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_SIZE;
import static com.chua.utils.tools.constant.StringConstant.EXTENSION_EMPTY;
import static com.chua.utils.tools.constant.StringConstant.EXTENSION_NULL;

/**
 * Map工具类
 *
 * @author CH
 */
public class MapHelper {

    /**
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap();
    }

    /**
     * 创建 ArrayListMultimap
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ArrayListMultimap<K, V> newArrayListMultimap() {
        return ArrayListMultimap.<K, V>create();
    }

    /**
     * 创建 HashMultimap
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMultimap<K, V> newHashMultimap() {
        return HashMultimap.<K, V>create();
    }

    /**
     * 创建 HashMultimap
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> LinkedListMultimap<K, V> newLinkedListMultimap() {
        return LinkedListMultimap.<K, V>create();
    }

    /**
     * 创建 HashBiMap
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashBiMap<K, V> newHashBiMap() {
        return HashBiMap.<K, V>create();
    }

    /**
     * 创建 TreeRangeSet
     *
     * @return
     */
    public static TreeRangeSet newTreeRangeSet() {
        return TreeRangeSet.create();
    }

    /**
     * 创建hashmap
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>(DEFAULT_SIZE);
    }

    /**
     * 创建hashmap
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> newHashMap(int initialCapacity) {
        return new HashMap<>(initialCapacity);
    }


    /**
     * 创建Map<K, V>
     *
     * @return
     */
    public static <K, V> Map<K, V> newMap() {
        return new HashMap<>(DEFAULT_SIZE);
    }

    /**
     * 创建Map<K, V>
     *
     * @return
     */
    public static <K, V> Map<K, V> newMap(Map<K, V> map) {
        if (BooleanHelper.isEmpty(map)) {
            return newMap();
        }
        return map;
    }

    /**
     * 创建Map<K, V>
     *
     * @return
     */
    public static Map<Object, Object> create() {
        return newMap();
    }

    /**
     * properties 转 map
     *
     * @param properties 原始数据
     * @return
     */
    public static Map<String, Object> maps(final Properties properties) {
        if (null == properties) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            result.put(entry.getKey().toString(), entry.getValue());
        }
        return result;
    }

    /**
     * map 转 properties
     *
     * @param source 原始数据
     * @return
     */
    public static <K, V> Properties properties(final Map<K, V> source) {
        if (null == source) {
            return null;
        }

        Properties properties = new Properties();
        for (Map.Entry<K, V> entry : source.entrySet()) {
            properties.put(entry.getKey().toString(), ObjectHelper.defaultIfNull(entry.getValue(), ""));
        }
        return properties;
    }

    /**
     * 初始化
     * <p>
     * {"test": "test"}
     * </p>
     *
     * @param values 值
     * @return
     */
    public static Map<String, Object> newMap(String values) {
        if (null == values || values.length() == 0) {
            return newMap();
        }

        return JsonHelper.fromJson2Map(values);
    }

    /**
     * 创建Map<K, V>
     *
     * @return
     */
    public static <K, V> Map<K, V> newMap(final int size) {
        return new HashMap<>(size);
    }

    /**
     * 创建Properties
     *
     * @return
     */
    public static Map<Object, Object> newMapStringObject() {
        return newMap();
    }

    /**
     * 创建Properties
     *
     * @return
     */
    public static Map<Object, Object> newMapObjectObject() {
        return newMap();
    }

    /**
     * 创建Properties
     *
     * @return
     */
    public static Map<String, List<Map<String, Object>>> newArrayList() {
        return new HashMap<>(DEFAULT_SIZE);
    }

    /**
     * 创建Map<String, String>
     *
     * @return
     */
    public static Map<String, String> newMapStringString() {
        return newMap();
    }

    /**
     * map长度
     * <p>
     * MapHelper.mapSize({}) = 0
     * MapHelper.mapSize(null) = 0
     * MapHelper.mapSize({1 : 1}) = 1
     * </p>
     *
     * @param map 原数据
     * @param <V>
     * @param <K>
     * @return
     */
    public static <V, K> int mapSize(final Map<K, V> map) {
        return null == map ? 0 : map.size();
    }

    /**
     * 添加值
     * <pre>
     *     MapHelper.put({}, null, 1) = {}
     *     MapHelper.put({}, 1, 1) = {1: 1}
     *     MapHelper.put(null, 1, 1) = {1: 1}
     * </pre>
     *
     * @param t     元数据
     * @param key   索引
     * @param value 值
     * @param <V>
     * @param <K>
     */
    public static <V, K> void put(Map<K, V> t, final K key, final V value) {
        if (BooleanHelper.isEmpty(t)) {
            t = newMap();
        }
        if (null != key) {
            t.put(key, value);
        }
    }

    /**
     * 添加集合
     * <pre>
     *     MapHelper.putAll({}, null) = {}
     *     MapHelper.putAll({}, {1: 1}) = {1: 1}
     *     MapHelper.putAll(null, {}) = {}
     *     MapHelper.putAll(null, null) = {}
     * </pre>
     *
     * @param source 原数据
     * @param target 目标数据
     */
    public static <K, V> void putAll(Map<K, V> source, final Map<K, V> target) {
        if (BooleanHelper.isEmpty(source)) {
            source = new HashMap<>();
        }

        if (BooleanHelper.isEmpty(target)) {
            return;
        }

        source.putAll(target);

    }


    /**
     * 获取所有key
     * <pre>
     *     MapHelper.keyList({}) = []
     *     MapHelper.keyList(null) = []
     *     MapHelper.keyList({1:1}) = [1]
     * </pre>
     *
     * @param t   原始数据
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> List<K> keyList(final Map<K, V> t) {
        if (BooleanHelper.isEmpty(t)) {
            return ListHelper.newArrayList();
        }
        return ListHelper.toList(t.keySet());
    }

    /**
     * 获取所有 value
     * <pre>
     *     MapHelper.keyList({}) = []
     *     MapHelper.keyList(null) = []
     *     MapHelper.keyList({1:1}) = [1]
     * </pre>
     *
     * @param source 原始数据
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> List<V> valueList(final Map<K, V> source) {
        if (BooleanHelper.isEmpty(source)) {
            return ListHelper.newArrayList();
        }
        return ListHelper.toList(source.values());
    }

    /**
     * 返回Object类型
     *
     * @param maps 原始数据
     * @param keys 关键词
     * @return
     */
    public static final Object objects(final List<String> keys, final Properties... maps) {
        if (!BooleanHelper.hasLength(keys)) {
            return null;
        }
        for (String key : keys) {
            Object objects = objects(key, maps);
            if (null == objects) {
                continue;
            }
            return objects;
        }
        return null;
    }

    /**
     * 返回Object类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final Object objects(final String key, final Properties... maps) {
        if (!BooleanHelper.hasLength(maps)) {
            return null;
        }

        for (Properties objectMap : maps) {
            Object objects1 = objectMap.get(key);
            if (null != objects1) {
                return objects1.toString();
            }
        }
        return null;
    }

    /**
     * 返回Object类型
     *
     * @param maps 原始数据
     * @param keys 关键词
     * @return
     */
    public static final Object objects(final List<String> keys, final Map<String, Object>... maps) {
        if (!BooleanHelper.hasLength(keys)) {
            return null;
        }
        for (String key : keys) {
            Object objects = objects(key, maps);
            if (null == objects) {
                continue;
            }
            return objects;
        }
        return null;
    }

    /**
     * 返回Object类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final Object objects(final String key, final Map<String, Object>... maps) {
        if (!BooleanHelper.hasLength(maps)) {
            return null;
        }

        for (Map<String, Object> objectMap : maps) {
            Object objects1 = objectMap.get(key);
            if (null != objects1) {
                return objects1;
            }
        }

        return null;
    }

    /**
     * 返回 String 类型
     * 默认 ""
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final String strings(final String key, final Properties... maps) {
        return strings(key, EXTENSION_EMPTY, maps);
    }

    /**
     * 返回 String 类型
     * 默认 ""
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final String strings(final String key, final Map<String, Object>... maps) {
        return strings(key, EXTENSION_EMPTY, maps);
    }

    /**
     * 返回 String 类型
     * 默认null
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final String stringsNull(final String key, final Properties... maps) {
        return strings(key, EXTENSION_NULL, maps);
    }

    /**
     * 返回 String 类型
     * 默认null
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final String stringsNull(final String key, final Map<String, Object>... maps) {
        return strings(key, EXTENSION_NULL, maps);
    }

    /**
     * 返回 String 类型
     *
     * @param key          关键词
     * @param defaultValue 默认值
     * @param maps
     * @return
     */
    public static final String strings(final String key, final String defaultValue, final Properties... maps) {
        final Object objects = objects(key, maps);
        return null == objects ? defaultValue : objects.toString();
    }

    /**
     * 返回 String 类型
     *
     * @param keys 关键词
     * @param maps
     * @return
     */
    public static final String strings(final List<String> keys, final Properties... maps) {
        return strings(keys, null, maps);
    }

    /**
     * 返回 String 类型
     *
     * @param keys 关键词
     * @param maps
     * @return
     */
    public static final String strings(final List<String> keys, final Map<String, Object>... maps) {
        return strings(keys, null, maps);
    }

    /**
     * 返回 String 类型
     *
     * @param keys         关键词
     * @param defaultValue 默认值
     * @param maps
     * @return
     */
    public static final String strings(final List<String> keys, final String defaultValue, final Properties... maps) {
        final Object objects = objects(keys, maps);
        return null == objects ? defaultValue : objects.toString();
    }

    /**
     * 返回 String 类型
     *
     * @param key          关键词
     * @param defaultValue 默认值
     * @param maps
     * @return
     */
    public static final String strings(final String key, final String defaultValue, final Map<String, Object>... maps) {
        final Object objects = objects(key, maps);
        return null == objects ? defaultValue : objects.toString();
    }

    /**
     * 返回 String 类型
     *
     * @param keys         关键词
     * @param defaultValue 默认值
     * @param maps
     * @return
     */
    public static final String strings(final List<String> keys, final String defaultValue, final Map<String, Object>... maps) {
        final Object objects = objects(keys, maps);
        return null == objects ? defaultValue : objects.toString();
    }

    /**
     * 返回 int 类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final int ints(final String key, final Properties... maps) {
        return ints(key, -1, maps);
    }

    /**
     * 返回 int 类型
     *
     * @param maps 原始数据
     * @param keys 关键词
     * @return
     */
    public static final int ints(final List<String> keys, final Properties... maps) {
        return ints(keys, -1, maps);
    }

    /**
     * 返回 int 类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final int ints(final String key, final Map<String, Object>... maps) {
        return ints(key, -1, maps);
    }

    /**
     * 返回 int 类型
     *
     * @param maps         原始数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static int ints(final String key, final int defaultValue, final Properties... maps) {
        final Object objects = objects(key, maps);
        return TypeHelper.toInt(objects, defaultValue);
    }

    /**
     * 返回 int 类型
     *
     * @param maps         原始数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static int ints(final String key, final int defaultValue, final Map<String, Object>... maps) {
        final Object objects = objects(key, maps);
        return TypeHelper.toInt(objects, defaultValue);
    }

    /**
     * 返回 int 类型
     *
     * @param keys 关键词数组
     * @param maps 原数据
     * @return
     */
    public static int ints(final List<String> keys, final int defaultValue, final Properties... maps) {
        final Object objects = objects(keys, maps);
        return null == objects ? defaultValue : TypeHelper.toInt(objects, defaultValue);
    }

    /**
     * 返回 int 类型
     *
     * @param keys 关键词数组
     * @param maps 原数据
     * @return
     */
    public static int ints(final List<String> keys, final int defaultValue, final Map<String, Object>... maps) {
        final Object objects = objects(keys, maps);
        return null == objects ? defaultValue : TypeHelper.toInt(objects, defaultValue);
    }

    /**
     * 返回 long 类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final long longs(final String key, final Properties... maps) {
        return longs(key, -1L, maps);
    }

    /**
     * 返回 long 类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final long longs(final String key, final Map<String, Object>... maps) {
        return longs(key, -1L, maps);
    }

    /**
     * 返回 long 类型
     *
     * @param maps         源数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static final long longs(final String key, final long defaultValue, final Properties... maps) {
        final Object objects = objects(key, maps);
        return TypeHelper.toLong(objects, defaultValue);
    }

    /**
     * 返回 long 类型
     *
     * @param maps         源数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static final long longs(final String key, final long defaultValue, final Map<String, Object>... maps) {
        final Object objects = objects(key, maps);
        return TypeHelper.toLong(objects, defaultValue);
    }

    /**
     * 返回 long 类型
     *
     * @param keys 关键词数组
     * @param maps 原数据
     * @return
     */
    public static long longs(final List<String> keys, final long defaultValue, final Properties... maps) {
        final Object objects = objects(keys, maps);
        return null == objects ? defaultValue : TypeHelper.toLong(objects, defaultValue);
    }

    /**
     * 返回 long 类型
     *
     * @param keys 关键词数组
     * @param maps 原数据
     * @return
     */
    public static long longs(final List<String> keys, final long defaultValue, final Map<String, Object>... maps) {
        final Object objects = objects(keys, maps);
        return null == objects ? defaultValue : TypeHelper.toLong(objects, defaultValue);
    }

    /**
     * 返回 float 类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final float floats(final String key, final Properties... maps) {
        return floats(key, -1f, maps);
    }

    /**
     * 返回 float 类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final float floats(final String key, final Map<String, Object>... maps) {
        return floats(key, -1f, maps);
    }

    /**
     * 返回 float 类型
     *
     * @param maps         源数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static final float floats(final String key, final float defaultValue, final Properties... maps) {
        final Object objects = objects(key, maps);
        return TypeHelper.toFloat(objects, defaultValue);
    }

    /**
     * 返回 float 类型
     *
     * @param maps         源数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static final float floats(final String key, final float defaultValue, final Map<String, Object>... maps) {
        final Object objects = objects(key, maps);
        return TypeHelper.toFloat(objects, defaultValue);
    }

    /**
     * 返回 float 类型
     *
     * @param keys 关键词数组
     * @param maps 原数据
     * @return
     */
    public static float floats(final List<String> keys, final float defaultValue, final Properties... maps) {
        final Object objects = objects(keys, maps);
        return null == objects ? defaultValue : TypeHelper.toFloat(objects, defaultValue);
    }

    /**
     * 返回 float 类型
     *
     * @param keys 关键词数组
     * @param maps 原数据
     * @return
     */
    public static float floats(final List<String> keys, final float defaultValue, final Map<String, Object>... maps) {
        final Object objects = objects(keys, maps);
        return null == objects ? defaultValue : TypeHelper.toFloat(objects, defaultValue);
    }

    /**
     * 返回 double 类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final double doubles(final String key, final Properties... maps) {
        return doubles(key, -1d, maps);
    }

    /**
     * 返回 double 类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final double doubles(final String key, final Map<String, Object>... maps) {
        return doubles(key, -1d, maps);
    }

    /**
     * 返回 double 类型
     *
     * @param maps         源数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static final double doubles(final String key, final double defaultValue, final Properties... maps) {
        final Object objects = objects(key, maps);
        return TypeHelper.toDouble(objects, defaultValue);
    }

    /**
     * 返回 double 类型
     *
     * @param maps         源数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static final double doubles(final String key, final double defaultValue, final Map<String, Object>... maps) {
        final Object objects = objects(key, maps);
        return TypeHelper.toDouble(objects, defaultValue);
    }

    /**
     * 返回 double 类型
     *
     * @param keys 关键词数组
     * @param maps 原数据
     * @return
     */
    public static double doubles(final List<String> keys, final double defaultValue, final Properties... maps) {
        final Object objects = objects(keys, maps);
        return null == objects ? defaultValue : TypeHelper.toDouble(objects, defaultValue);
    }

    /**
     * 返回 double 类型
     *
     * @param keys 关键词数组
     * @param maps 原数据
     * @return
     */
    public static double doubles(final List<String> keys, final double defaultValue, final Map<String, Object>... maps) {
        final Object objects = objects(keys, maps);
        return null == objects ? defaultValue : TypeHelper.toDouble(objects, defaultValue);
    }

    /**
     * 返回boolean类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final boolean booleans(final String key, final Properties... maps) {
        return booleans(key, false, maps);
    }

    /**
     * 返回boolean类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final boolean booleans(final String key, final Map<String, Object>... maps) {
        return booleans(key, false, maps);
    }

    /**
     * 返回boolean类型
     *
     * @param maps         原始数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static final boolean booleans(final String key, final boolean defaultValue, final Properties... maps) {
        final String string = strings(key, maps);
        return null == string ? defaultValue : BooleanHelper.toBoolean(string);
    }

    /**
     * 返回boolean类型
     *
     * @param maps         原始数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static final boolean booleans(final String key, final boolean defaultValue, final Map<String, Object>... maps) {
        final String string = strings(key, maps);
        return null == string ? defaultValue : BooleanHelper.toBoolean(string);
    }

    /**
     * 返回Class类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static Class<?> classes(final String key, final Properties... maps) {
        return classes(key, null, maps);
    }

    /**
     * 返回Class类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static Class<?> classes(final String key, final Map<String, Object>... maps) {
        return classes(key, null, maps);
    }

    /**
     * 返回Class类型
     *
     * @param maps         原始数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static Class<?> classes(final String key, final Class<?> defaultValue, final Properties... maps) {
        Object objects = objects(key, maps);
        return null == objects ? defaultValue : objects instanceof Class ? (Class<?>) objects : defaultValue;
    }

    /**
     * 返回Class类型
     *
     * @param maps         原始数据
     * @param key          关键词
     * @param defaultValue 默认值
     * @return
     */
    public static Class<?> classes(final String key, final Class<?> defaultValue, final Map<String, Object>... maps) {
        Object objects = objects(key, maps);
        return null == objects ? defaultValue : objects instanceof Class ? (Class<?>) objects : defaultValue;
    }

    /**
     * 返回Map类型
     *
     * @param maps        原始数据
     * @param key         索引
     * @param entityClass 类
     * @return
     */
    public static final <T> T entity(final String key, final Class<T> entityClass, final Properties... maps) {
        return entity(key, entityClass, null, maps);
    }

    /**
     * 返回Map类型
     *
     * @param maps        原始数据
     * @param key         索引
     * @param entityClass 类
     * @return
     */
    public static final <T> T entity(final String key, final Class<T> entityClass, final Map<String, Object>... maps) {
        return entity(key, entityClass, null, maps);
    }

    /**
     * 返回Map类型
     *
     * @param maps         原始数据
     * @param key          索引
     * @param entityClass  类
     * @param defaultValue 默认值
     * @return
     */
    public static final <T> T entity(final String key, final Class<T> entityClass, final T defaultValue, final Properties... maps) {
        Object objects = objects(key, maps);
        if (null == objects) {
            return defaultValue;
        }

        if (objects instanceof String) {
            Class<?> aClass = ClassHelper.forName(objects.toString());
            return entityClass.isAssignableFrom(aClass) ? (T) ClassHelper.forObject(aClass) : defaultValue;
        }

        if (objects instanceof Class) {
            Class<?> aClass = (Class<?>) objects;
            return entityClass.isAssignableFrom(aClass) ? (T) ClassHelper.forObject(aClass) : defaultValue;
        }

        if (objects instanceof Object) {
            return objects.getClass().isAssignableFrom(entityClass) ? (T) objects : defaultValue;
        }
        return defaultValue;
    }

    /**
     * 返回Map类型
     *
     * @param maps         原始数据
     * @param key          索引
     * @param entityClass  类
     * @param defaultValue 默认值
     * @return
     */
    public static final <T> T entity(final String key, final Class<T> entityClass, final T defaultValue, final Map<String, Object>... maps) {
        Object objects = objects(key, maps);
        if (null == objects) {
            return defaultValue;
        }

        if (objects instanceof String) {
            Class<?> aClass = ClassHelper.forName(objects.toString());
            return entityClass.isAssignableFrom(aClass) ? (T) ClassHelper.forObject(aClass) : defaultValue;
        }

        if (objects instanceof Class) {
            Class<?> aClass = (Class<?>) objects;
            return entityClass.isAssignableFrom(aClass) ? (T) ClassHelper.forObject(aClass) : defaultValue;
        }

        if (objects instanceof Object) {
            return objects.getClass().isAssignableFrom(entityClass) ? (T) objects : defaultValue;
        }
        return defaultValue;
    }

    /**
     * 返回Map类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final <K, V> Map<K, V> maps(final String key, final Properties... maps) {
        Object objects = objects(key, maps);
        return objects instanceof Map ? (Map<K, V>) objects : Collections.<K, V>emptyMap();
    }

    /**
     * 返回Map类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final <K, V> Map<K, V> maps(final String key, final Map<String, Object>... maps) {
        Object objects = objects(key, maps);
        return objects instanceof Map ? (Map<K, V>) objects : Collections.<K, V>emptyMap();
    }

    /**
     * 返回List类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final List<Object> lists(final String key, final Properties... maps) {
        return lists(key, Object.class, maps);
    }

    /**
     * 返回List类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static final List<Object> lists(final String key, final Map<String, Object>... maps) {
        return lists(key, Object.class, maps);
    }

    /**
     * 返回List类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static <T> List<T> lists(final String key, Class<T> type, final Properties... maps) {
        Object objects = objects(key, maps);
        if (null == objects) {
            return Collections.emptyList();
        }
        if (BooleanHelper.isCollection(objects)) {
            List<Object> items = (List<Object>) objects;
            if (items.size() > 0) {
                Object o = items.get(0);
                if (o.getClass().getName().equals(type.getName())) {
                    return (List<T>) objects;
                }
            }
            return null;
        } else if (BooleanHelper.isArray(objects)) {
            List<Object> items = Arrays.asList(objects);
            if (items.size() > 0) {
                Object o = items.get(0);
                if (o.getClass().getName().equals(type.getName())) {
                    return Arrays.asList((T[]) objects);
                }
            }
            return null;
        } else if (type.isAssignableFrom(objects.getClass())) {
            return (List<T>) Lists.newArrayList(objects);
        }
        return Collections.emptyList();
    }

    /**
     * 返回Object[]类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static <T> Object[] arrays(final String key, final Map<String, Object>... maps) {
        Object objects = objects(key, maps);
        if (null == objects) {
            return new Object[0];
        }
        if(objects instanceof Array) {
            return (Object[]) objects;
        }
        return new Object[0];
    }
    /**
     * 返回Object[]类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static String[] stringArrays(final String key, final Map<String, Object>... maps) {
        Object objects = objects(key, maps);
        if (null == objects) {
            return  ArraysHelper.emptyStringArray();
        }
        if(BooleanHelper.isArray(objects)) {
            Object obj = FinderHelper.firstElement((Object[]) objects);
            return obj instanceof String ? (String[]) objects : ArraysHelper.emptyStringArray();
        }
        return  ArraysHelper.emptyStringArray();
    }
    /**
     * 返回List类型
     *
     * @param maps 原始数据
     * @param key  关键词
     * @return
     */
    public static <T> List<T> lists(final String key, Class<T> type, final Map<String, Object>... maps) {
        Object objects = objects(key, maps);
        if (null == objects) {
            return Collections.emptyList();
        }
        if (BooleanHelper.isCollection(objects)) {
            List<Object> items = (List<Object>) objects;
            if (items.size() > 0) {
                Object o = items.get(0);
                if (o.getClass().getName().equals(type.getName())) {
                    return (List<T>) objects;
                }
            }
            return null;
        } else if (BooleanHelper.isArray(objects)) {
            List<Object> items = Arrays.asList(objects);
            if (items.size() > 0) {
                Object o = items.get(0);
                if (o.getClass().getName().equals(type.getName())) {
                    return Arrays.asList((T[]) objects);
                }
            }
            return null;
        } else if (type.isAssignableFrom(objects.getClass())) {
            return (List<T>) Lists.newArrayList(objects);
        }
        return Collections.emptyList();
    }

    /**
     * 合并数据
     * <p>
     * MapHelper.combine({}, {key1: value1}) => {key1: value1}
     * MapHelper.combine(null, {key1: value1}) => {key1: value1}
     * MapHelper.combine({key1: value1}, null) => {key1: value1}
     * </p>
     *
     * @param source 原始数据
     * @param target 目标数据
     */
    public static <K, V> void combine(Map<K, V> source, final Map<K, V> target) {
        if (BooleanHelper.isEmpty(target)) {
            return;
        }

        if (BooleanHelper.isEmpty(source)) {
            source = Maps.newHashMap();
        }
        source.putAll(target);
    }

    /**
     * 合并数据
     * <p>
     * MapHelper.combine({}, "key1", "value1") => {key1: value1}
     * MapHelper.combine(null, "key1", "key2") => null
     * MapHelper.combine({key3: value3}, "key1", "value1") => {key3: value3, key1: value1}
     * </p>
     *
     * @param source 原始数据
     * @param key    新数据索引
     * @param value  新数据值
     */
    public static <K, V> void combine(Map<K, V> source, K key, V value) {
        if (null != key && null != value) {
            source.put(key, value);
        }
    }

    /**
     * map数据赋值到obj
     *
     * @param obj    对象
     * @param source 原始数据
     */
    public static <T> T entity(final T obj, final Map<String, Object> source) {
        if (null == obj || !BooleanHelper.hasLength(source)) {
            return null;
        }
        return BeansHelper.setProperty(obj, source);
    }

    /**
     * key替换
     * <p>
     * MapHelper.replaceKey({}, "key1", "key2") => {}
     * MapHelper.replaceKey(null, "key1", "key2") => null
     * MapHelper.replaceKey({key3: value3}, "key1", "key2") => {key3: value3}
     * MapHelper.replaceKey({key1: value3}, "key1", "key2")  => {key2: value3}
     * </p>
     *
     * @param map    原始数据集
     * @param newKey 新值
     * @param oldKey 原始值
     */
    public static <K, V> void replaceKey(final Map<K, V> map, final K oldKey, final K newKey) {
        if (!BooleanHelper.hasLength(map) || !map.containsKey(oldKey)) {
            return;
        }
        V v = map.get(oldKey);
        map.remove(oldKey);
        map.put(newKey, v);
    }

    /**
     * 解析对象到properties
     *
     * @throws
     * @author CH
     * @updateTime 2020/5/31 13:42
     */
    public static Properties multiMapToProperties(ArrayListMultimap<String, Object> node) {
        Map<String, Collection<Object>> collectionMap = node.asMap();
        Properties properties = new Properties();
        for (Map.Entry<String, Collection<Object>> entry : collectionMap.entrySet()) {
            List<Object> value = (List<Object>) entry.getValue();
            String key = entry.getKey();
            if (value.size() > 1) {
                analysisValuesProperties(key, value, properties);
            } else {
                Object element = FinderHelper.firstElement(value);
                if (element instanceof String && isNoValid(element)) {
                    continue;
                }
                properties.put(key, element);
            }
        }
        return properties;
    }

    /**
     * 解析对象到properties
     *
     * @throws
     * @author CH
     * @updateTime 2020/5/31 13:42
     */
    private static void analysisValuesProperties(final String key, final List<Object> value, final Properties properties) {
        int length = value.size();
        for (int i = 0; i < length; i++) {
            Object element = value.get(i);
            if (element instanceof String && isNoValid(element)) {
                continue;
            }
            properties.put(key + "[" + i + "]", element);
        }
    }

    /**
     * 无效数据
     *
     * @throws
     * @author CH
     * @updateTime 2020/5/31 13:53
     */
    public static boolean isNoValid(Object value) {
        if (null == value) {
            return true;
        }
        String string = value.toString();
        return string.indexOf("\n") > -1 && "".equals(string.trim());
    }

    /**
     * properties转Properties
     *
     * @param properties
     * @return
     */
    public static Map<String, Object> properties2Map(final Properties properties) {
        if (!BooleanHelper.hasLength(properties)) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            result.put(entry.getKey().toString(), entry.getValue());
        }
        return result;
    }

    /**
     * 获取数据
     *
     * @param key        索引
     * @param properties 源数据
     * @param wildcard   是否通配符
     * @return
     */
    public static List<Object> queryList(String key, Properties properties, boolean wildcard) {
        List<Object> result = new ArrayList<>();
        if (!wildcard) {
            result.add(properties.get(key));
            return result;
        }

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            Object key1 = entry.getKey();
            if (StringHelper.wildcardMatch(key1.toString(), key)) {
                result.add(entry.getValue());
            }
        }

        return result;
    }

    /**
     * @param load
     * @return
     */
    public static Properties map2Yaml(Object load) {
        Properties properties1 = new Properties();
        analysisMap(load, properties1);
        return properties1;
    }

    /**
     * @param load
     * @param result
     */
    private static void analysisMap(Object load, final Properties result) {
        if (load instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) load;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                dataFormatYaml(key, value, result);
            }
        }
    }

    /**
     * 数据格式化成yaml格式
     *
     * @param key
     * @param value
     * @param result
     */
    private static void dataFormatYaml(String key, Object value, Properties result) {
        if (value instanceof Map) {
            doAnalysisMapValue(key, value, result);
        } else if (value instanceof List) {
            doAnalysisListValue(key, value, result);
        } else {
            result.put(key, value);
        }
    }

    /**
     * 解析value
     *
     * @param parentName
     * @param valueObject
     * @param result
     */
    private static void doAnalysisListValue(String parentName, Object valueObject, Properties result) {
        if (valueObject instanceof List) {
            List<Object> list = (List<Object>) valueObject;
            int size = list.size();
            if (size == 1) {
                result.put(parentName, list.get(0));
                return;
            }
            for (int i = 0; i < size; i++) {
                result.put(parentName + "[" + i + "]", list.get(i));
            }

            result.put(parentName, list);
        }
    }

    /**
     * 循环解析
     *
     * @param parentName
     * @param valueObject
     * @param result
     */
    private static void doAnalysisMapValue(String parentName, Object valueObject, Properties result) {
        if (valueObject instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) valueObject;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                dataFormatYaml(parentName + "." + key, value, result);
            }
        }
    }

    /**
     * Map<String, List<Properties>>
     *
     * @return
     */
    public static Map<String, List<Properties>> newMapListMap() {
        return Maps.newHashMap();
    }

    /**
     * 类型转为 ConcurrentMap
     *
     * @param properties
     * @return
     */
    public static ConcurrentMap<String, Object> toConcurrentMap(Properties properties) {
        ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
        if (null == properties) {
            return concurrentHashMap;
        }
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            concurrentHashMap.put(entry.getKey().toString(), entry.getValue());
        }
        return concurrentHashMap;
    }

    /**
     * 类型转为 ConcurrentMap
     *
     * @param source
     * @return
     */
    public static ConcurrentMap<String, Object> toConcurrentMap(Map<String, Properties> source) {
        ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
        if (null == source) {
            return concurrentHashMap;
        }
        for (Properties properties : source.values()) {
            concurrentHashMap.putAll(toConcurrentMap(properties));
        }
        for (Map.Entry<String, Properties> entry : source.entrySet()) {
            for (Map.Entry<Object, Object> objectObjectEntry : entry.getValue().entrySet()) {
                concurrentHashMap.put(objectObjectEntry.getKey().toString(), objectObjectEntry.getValue());
            }
        }
        return concurrentHashMap;
    }

    /**
     * 是否存在
     *
     * @param key    索引
     * @param extMap
     * @return
     */
    public static boolean isValid(final String key, final Properties... extMap) {
        for (Properties stringObjectMap : extMap) {
            if (stringObjectMap.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在
     *
     * @param key    索引
     * @param extMap
     * @return
     */
    public static <K, V> boolean isValid(final String key, final Map<K, V>... extMap) {
        for (Map<K, V> stringObjectMap : extMap) {
            if (stringObjectMap.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含数据
     *
     * @param source 元数据
     * @param index  索引
     * @return
     */
    public static boolean containerKey(Map<String, MethodIntercept> source, String index) {
        return null != source && source.containsKey(index);
    }
}

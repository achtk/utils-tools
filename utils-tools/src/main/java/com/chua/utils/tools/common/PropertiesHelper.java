package com.chua.utils.tools.common;

import com.chua.utils.tools.common.loader.PropertiesLoader;
import com.google.common.collect.HashMultimap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * properties工具类
 * @author CH
 * @since 1.0
 */
public class PropertiesHelper {
    private static String DOT = ".";

    /**
     * 获取int数据
     * @param properties 原始数据
     * @param index 索引
     * @return
     */
    public static int ints(Properties properties, String index) {
        return ints(properties, index, -1);
    }
    /**
     * 获取int数据
     * @param properties 原始数据
     * @param index 索引
     * @param defaultValue 默认值
     * @return
     */
    public static int ints(Properties properties, String index, int defaultValue) {
        if(BooleanHelper.isValid(properties, index)) {
            return defaultValue;
        }
        String property = properties.getProperty(index);
        if(!BooleanHelper.isNumeric(property)) {
            return defaultValue;
        }

        return NumberHelper.toInt(property);
    }
    /**
     * 获取long数据
     * @param properties 原始数据
     * @param index 索引
     * @return
     */
    public static long longs(Properties properties, String index) {
        return longs(properties, index, -1);
    }
    /**
     * 获取long数据
     * @param properties 原始数据
     * @param index 索引
     * @param defaultValue 默认值
     * @return
     */
    public static long longs(Properties properties, String index, long defaultValue) {
        if(BooleanHelper.isValid(properties, index)) {
            return defaultValue;
        }
        String property = properties.getProperty(index);
        if(!BooleanHelper.isNumeric(property)) {
            return defaultValue;
        }

        return NumberHelper.toLong(property);
    }
    /**
     * 获取float数据
     * @param properties 原始数据
     * @param index 索引
     * @return
     */
    public static float floats(Properties properties, String index) {
        return floats(properties, index, -1);
    }
    /**
     * 获取float数据
     * @param properties 原始数据
     * @param index 索引
     * @param defaultValue 默认值
     * @return
     */
    public static float floats(Properties properties, String index, float defaultValue) {
        if(BooleanHelper.isValid(properties, index)) {
            return defaultValue;
        }
        String property = properties.getProperty(index);
        if(!BooleanHelper.isNumeric(property)) {
            return defaultValue;
        }

        return NumberHelper.toFloat(property);
    }
    /**
     * 获取double数据
     * @param properties 原始数据
     * @param index 索引
     * @return
     */
    public static double doubles(Properties properties, String index) {
        return doubles(properties, index, -1);
    }
    /**
     * 获取float数据
     * @param properties 原始数据
     * @param index 索引
     * @param defaultValue 默认值
     * @return
     */
    public static double doubles(Properties properties, String index, double defaultValue) {
        if(BooleanHelper.isValid(properties, index)) {
            return defaultValue;
        }
        String property = properties.getProperty(index);
        if(!BooleanHelper.isNumeric(property)) {
            return defaultValue;
        }

        return NumberHelper.toDouble(property);
    }
    /**
     * 获取string数据
     * @param properties 原始数据
     * @param index 索引
     * @return
     */
    public static String strings(Properties properties, String index) {
        return strings(properties, index, null);
    }
    /**
     * 获取string数据
     * @param properties 原始数据
     * @param index 索引
     * @param defaultValue 默认值
     * @return
     */
    public static String strings(Properties properties, String index, String defaultValue) {
        if(BooleanHelper.isValid(properties, index)) {
            return defaultValue;
        }
        return properties.getProperty(index);
    }

    /**
     * 获取boolean值
     * @param properties 原数据
     * @param index 索引
     * @return
     */
    public static boolean booleans(Properties properties, String index) {
        return BooleanHelper.isValid(properties, index) ? false : BooleanHelper.toBoolean(properties.getProperty(index));
    }

    /**
     * 数组
     * @param properties
     * @param index
     * @return
     */
    public static String[] arrays(Properties properties, String index) {
        if(!BooleanHelper.isValid(properties, index)) {
            return null;
        }
        Object o = properties.get(index);
        if(o instanceof List) {
            List<String> lists = (List<String>) o;
            return lists.toArray(new String[lists.size()]);
        } else if(o.getClass().isArray()) {
            return (String[]) o;
        }
        return new String[] {o + ""};
    }

    /**
     * map转properties
     * @param source 数据源
     * @return
     */
    public static PropertiesLoader yaml2Properties(final Map<String, Object> source) {
        if(!BooleanHelper.hasLength(source)) {
            return null;
        }
        Properties properties = new Properties();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if(value instanceof List) {
                listIntoProperties(key, (List<Object>) value, properties);
                continue;
            }
            if(value instanceof Map) {
                mapIntoProperties(key, (Map<String, Object>) value, properties);
                continue;
            }
            properties.put(key, value);
        }
        return PropertiesLoader.newLoader(properties);
    }

    /**
     * properties转map
     * @param properties 数据源
     * @return
     */
    public static Map<String, Object> properties2Yaml(final Properties properties) {
        if(!BooleanHelper.hasLength(properties)) {
            return null;
        }
        HashMultimap<String, Object> hashMultimap = HashMultimap.create();
        HashMultimap<String, Object> level2Cache = HashMultimap.create();

        CopyOnWriteArrayList cache = new CopyOnWriteArrayList<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            int index = key.indexOf(".");
            if(index == -1) {
                hashMultimap.put(key, value);
                continue;
            }
            String startKey = key.substring(0, index);
            String newKey = key.substring(index + 1);
            if(newKey.endsWith("]")) {
                cache.add(newKey);
            }
            continueProcessing(newKey, value, level2Cache);
            hashMultimap.put(startKey, level2Cache);
        }
        return null;
    }

    /**
     * 深度处理
     * @param key
     * @param value
     * @param level2Cache
     * @return
     */
    private static void continueProcessing(String key, Object value, HashMultimap<String, Object> level2Cache) {
        if(key.endsWith("]")) {
            int index = key.indexOf("[");
            key = key.substring(0, index);
        }
        int index = key.indexOf(".");
        if(index == -1) {
            level2Cache.put(key, value);
            return;
        }

        String startKey = key.substring(0, index);
        String newKey = key.substring(0, index);
        HashMultimap<String, Object> levelMoreCache = HashMultimap.create();
        continueProcessing(newKey, value, levelMoreCache);
        level2Cache.put(startKey, levelMoreCache);
    }

    /**
     * list渲染到properties
     * @param key 上层索引
     * @param value 上层数据
     * @param properties properties
     */
    private static void mapIntoProperties(String key, Map<String, Object> value, Properties properties) {
        if (!BooleanHelper.hasLength(value)) {
            return;
        }
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            String key1 = entry.getKey();
            Object value1 = entry.getValue();
            String newKey = key + DOT + key1;
            if(value1 instanceof List) {
                listIntoProperties(newKey, (List<Object>) value1, properties);
                continue;
            }
            if(value1 instanceof Map) {
                mapIntoProperties(newKey, (Map<String, Object>) value1, properties);
                continue;
            }

            properties.put(newKey, value1);
        }
    }
    /**
     * list渲染到properties
     * @param key 上层索引
     * @param value 上层数据
     * @param properties properties
     */
    private static void listIntoProperties(String key, List<Object> value, Properties properties) {
        if(!BooleanHelper.hasLength(value)) {
            return;
        }

        for (int i = 0; i < value.size(); i++) {
            Object item = value.get(i);
            String newKey = key + "[" + i + "]";
            if(item instanceof List) {
                properties.put(newKey, JsonHelper.toJson(item));
                continue;
            }
            if(item instanceof Map) {
                properties.put(newKey, JsonHelper.toJson(item));
                continue;
            }
            properties.put(newKey, item);
        }
    }
}

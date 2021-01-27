package com.chua.utils.tools.util;

import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.function.impl.LevelsClose;
import com.chua.utils.tools.function.impl.LevelsOpen;
import com.google.common.base.Strings;

import java.util.*;
import java.util.stream.Collectors;

/**
 * map工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/6
 */
public class MapUtils extends MapOperableHelper {
    /**
     * 合并姐
     *
     * @param source 集合
     * @return 合并的集合
     */
    public static <K, V> Map<K, V> merge(final Map<K, V>... source) {
        if (null == source) {
            return Collections.emptyMap();
        }
        Map<K, V> result = new HashMap<>();
        for (Map<K, V> map : source) {
            if (isEmpty(map)) {
                continue;
            }
            result.putAll(map);
        }
        return result;
    }

    /**
     * 如果索引不存在则设置值
     *
     * @param source 集合
     * @param key    索引
     * @param value  值
     */
    public static <K, V> V putIfNotExist(Map<K, V> source, K key, V value) {
        if (null == source || source.containsKey(key)) {
            return null;
        }
        return source.put(key, value);
    }

    /**
     * 如果索引存在则设置值
     *
     * @param source 集合
     * @param key    索引
     * @param value  值
     */
    public static <K, V> V putIfExist(Map<K, V> source, K key, V value) {
        if (null == source || !source.containsKey(key)) {
            return null;
        }
        return source.put(key, value);
    }

    /**
     * 装配
     *
     * @param source 集合
     * @param tClass 装配类
     * @param mapper 字段映射
     * @param <K>    索引
     * @param <V>    值
     * @param <T>    类型
     * @return
     */
    public static <K, V, T> T assemblage(final Map<K, V> source, Class<T> tClass, final String... mapper) {
        if (null == tClass) {
            return null;
        }

        if (isEmpty(source)) {
            return ClassUtils.forObject(tClass);
        }
        return BeanCopy.of(tClass).with(source).create(mapper);
    }

    /**
     * 匹配Key
     *
     * @param source 集合
     * @param key    索引
     * @param <K>    索引类型
     * @param <V>    值类型
     * @return
     */
    public static <K, V> List<V> find(final Map<K, V> source, String key) {
        if (isEmpty(source) || Strings.isNullOrEmpty(key)) {
            return Collections.emptyList();
        }
        key = key.replace("#", "*");
        String finalKey = key;
        return source.entrySet().stream().map(kvEntry -> {
            K key1 = kvEntry.getKey();
            if (StringUtils.wildcardMatch(key1.toString(), finalKey)) {
                return kvEntry.getValue();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 层级压缩
     *
     * @param fromJson2Map Map
     * @return 单层Map
     */
    public static Map<String, Object> levelsMapClose(Map<String, Object> fromJson2Map) {
        return new LevelsClose().apply(fromJson2Map);
    }

    /**
     * 层级展开
     *
     * @param fromJson2Map Map
     * @return 多层Map
     */
    public static Map<String, Object> levelsMapOpen(Map<String, Object> fromJson2Map) {
        return new LevelsOpen().apply(fromJson2Map);
    }

    /**
     * 转化为Map
     *
     * @param source 数据
     * @return 是Map返回Map, 反之返回null
     */
    public static Map<String, Object> asMap(Object source) {
        return source instanceof Map ? (Map<String, Object>) source : null;
    }

    /**
     * 是否是Map
     *
     * @param source 数据
     * @return 是Map返回true
     */
    public static boolean isMap(Object source) {
        return source instanceof Map;
    }
}

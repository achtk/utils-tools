package com.chua.utils.tools.util;

import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author CH
 * @version 1.0.0
 * @see com.chua.utils.tools.collects.collections.CollectionHelper
 * @since 2020/12/22
 */
public class CollectionUtils extends CollectionHelper {
    /**
     * 获取索引对应的数据
     *
     * @param source 数据
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T findFirst(final Collection<T> source) {
        return find(source, 0);
    }

    /**
     * 获取索引对应的数据
     *
     * @param source 数据
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T findLast(final Collection<T> source) {
        return findReverse(source, -1);
    }

    /**
     * 获取索引对应的数据
     *
     * @param source 数据
     * @param index  索引
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T findAny(final Collection<T> source, final int index) {
        if (index > 0) {
            return find(source, index);
        }
        return findReverse(source, -1);
    }

    /**
     * 获取索引对应的数据
     *
     * @param source 数据
     * @param index  索引
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T findReverse(final Collection<T> source, final int index) {
        if (null == source || index > 0) {
            return null;
        }
        int length = source.size();
        int realIndex = length + index;
        if (realIndex > length) {
            return null;
        }
        int count = 0;
        for (T t : source) {
            if (count ++ == realIndex) {
                return t;
            }
        }
        return null;
    }

    /**
     * 获取索引对应的数据
     *
     * @param source 数据
     * @param index  索引
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T find(final Collection<T> source, final int index) {
        return find(source, index, null);
    }

    /**
     * 获取索引对应的数据
     *
     * @param source 数据
     * @param index  索引
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T find(final Collection<T> source, final int index, final T defaultValue) {
        if (null == source || index < 0) {
            return defaultValue;
        }
        int length = source.size();
        if (index > length) {
            return defaultValue;
        }
        int count = 0;
        for (T t : source) {
            if (count++ == index) {
                return t;
            }
        }
        return defaultValue;
    }

    /**
     * 有且只有唯一元素的集合返回唯一元素, 否则返回 null
     *
     * @param source 集合
     * @param <T>    元素
     * @return 有且只有唯一元素的集合返回唯一元素, 否则返回 null
     */
    public static <T> T findOnly(Map<?, T> source) {
        if (null == source || source.size() != 1) {
            return null;
        }
        return source.values().stream().findFirst().get();
    }

    /**
     * 有且只有唯一元素的集合返回唯一元素, 否则返回 null
     *
     * @param source 集合
     * @param <T>    元素
     * @return 有且只有唯一元素的集合返回唯一元素, 否则返回 null
     */
    public static <T> T findOnly(Collection<T> source) {
        if (null == source || source.size() != 1) {
            return null;
        }
        return source.stream().findFirst().get();
    }

    /**
     * 赋值
     *
     * @param map        集合
     * @param <V>        值类型
     * @param valueClass 值类型
     */
    public static <K, V> V putObject(final Map<K, V> map, final K key, final Class<V> valueClass) {
        return putObject(map, key, null == valueClass ? null : valueClass.getName(), valueClass);
    }

    /**
     * 赋值
     *
     * @param map        集合
     * @param objectName 对象名称
     * @param <V>        值类型
     * @param valueClass 值类型
     */
    public static <K, V> V putObject(final Map<K, V> map, final K key, final String objectName, final Class<V> valueClass) {
        if (null == map || Strings.isNullOrEmpty(objectName) || null == valueClass || null == key) {
            return null;
        }
        if (String.class.isAssignableFrom(valueClass)) {
            map.put(key, (V) objectName);
            return (V) objectName;
        }

        if (!ClassUtils.isPresent(objectName)) {
            return null;
        }

        V v = ClassUtils.forObject(objectName, valueClass);
        map.put(key, v);
        return v;
    }

    /**
     * 判断集合中是否存在索引, 不存在初始化并赋值并返回初始化数据
     *
     * @param key      索引
     * @param supplier 回调(用于处理索引不存在的数据初始化)
     * @param params   集合
     * @param <K>      索引类型
     * @param <V>      值类型
     * @return 值
     */
    public static <K, V> V get(K key, Supplier<V> supplier, Map<K, V> params) {
        if (null == params) {
            return supplier.get();
        }

        V v = null;
        if (!params.containsKey(key)) {
            v = supplier.get();
            params.put(key, v);
        } else {
            v = params.get(key);
        }
        return v;
    }
}

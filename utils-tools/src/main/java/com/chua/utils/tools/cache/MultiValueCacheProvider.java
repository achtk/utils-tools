package com.chua.utils.tools.cache;


import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.function.Filter;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * 缓存接口
 *
 * @author CH
 */
public interface MultiValueCacheProvider<K, V> extends CacheProvider<K, Set<V>> {

    /**
     * 获取第一个值
     *
     * @param k 索引
     * @return V
     */
    default V getFirst(K k) {
        return getIndex(k, 0);
    }

    /**
     * 获取第{index}个值
     *
     * @param k     索引
     * @param index 序号
     * @return V
     */
    V getIndex(K k, int index);

    /**
     * 是否包含值
     *
     * @param k 索引
     * @param v 值
     * @return boolean
     */
    default boolean containsValue(K k, V v) {
        Set<V> vs = get(k);
        return CollectionHelper.contains(vs, v);
    }

    /**
     * 添加值
     *
     * @param key   索引
     * @param value 值
     */
    default void add(K key, @Nullable V value) {
        Set<V> values = get(key);
        if (values == null) {
            values = new HashSet<>();
            put(key, values);
        }
        values.add(value);
    }

    /**
     * 添加值
     *
     * @param key    索引
     * @param values 值
     */
    default void addAll(K key, Set<? extends V> values) {
        Set<V> keyValues = get(key);
        if (keyValues == null) {
            keyValues = new HashSet<>();
            put(key, keyValues);
        }
        keyValues.addAll(values);
    }

    /**
     * 设置值
     *
     * @param key   索引
     * @param value 值
     */
    default void set(K key, @Nullable V value) {
        Set<V> values = new HashSet<>();
        values.add(value);
        put(key, values);
    }

    /**
     * 添加值(如果不存在)
     *
     * @param key   索引
     * @param value 值
     */
    default void addIfAbsent(K key, @Nullable V value) {
        if (!containsKey(key)) {
            add(key, value);
        }
    }

    /**
     * 过滤元素
     *
     * @param k      索引
     * @param filter 过滤器
     * @return List<V>
     */
    default Set<V> filter(K k, Filter<V> filter) {
        Set<V> vs = get(k);
        if (!BooleanHelper.hasLength(vs)) {
            return vs;
        }
        Set<V> result = new HashSet<>();
        vs.parallelStream().forEach(v -> {
            boolean matcher = filter.matcher(v);
            if (!matcher) {
                return;
            }
            result.add(v);
        });
        return result;
    }
}

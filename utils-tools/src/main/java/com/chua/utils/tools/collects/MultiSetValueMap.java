package com.chua.utils.tools.collects;

import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.function.Matcher;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 可重复值接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/17
 */
public interface MultiSetValueMap<K, V> extends Map<K, Set<V>> {
    /**
     * 获取第一个值
     *
     * @param key 索引
     * @return V
     */
    @Nullable
    default V getFirst(K key) {
        return find(key, 0);
    }

    /**
     * 获取第{index}个值
     *
     * @param k     索引
     * @param index 序号
     * @return V
     */
    default V find(K k, int index) {
        return FinderHelper.findElement(index, get(k));
    }

    /**
     * 过滤元素
     *
     * @param k      索引
     * @param filter 过滤器
     * @return List<V>
     */
    default Set<V> doWithFilter(K k, Filter<V> filter) {
        return CollectionHelper.doWithFilter(get(k), filter);
    }

    /**
     * 匹配元素
     *
     * @param k       索引
     * @param matcher 匹配器
     */
    default void doWithMatcher(K k, Matcher<V> matcher) {
        CollectionHelper.doWithMatcher(get(k), matcher);
    }

    /**
     * 添加值(存在索引即赋值)
     *
     * @param key   索引
     * @param value 值
     */
    void add(K key, @Nullable V value);

    /**
     * 添加值(存在索引即赋值)
     *
     * @param key    索引
     * @param values 值
     */
    void addAll(K key, Set<? extends V> values);

    /**
     * 添加值
     *
     * @param values 值
     */
    void addAll(MultiSetValueMap<K, V> values);

    /**
     * 设置值
     *
     * @param key   索引
     * @param value 值
     */
    void set(K key, @Nullable V value);

    /**
     * 设置值
     *
     * @param values 值
     */
    void setAll(Map<K, V> values);

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
     * 获取Map
     *
     * @param k 索引
     * @return Map<V, K>
     */
    default Map<V, K> asMap(K k) {
        Collection<V> vs = get(k);
        Map<V, K> result = new HashMap<>(vs.size());
        for (V v : vs) {
            result.put(v, k);
        }
        return result;
    }
}

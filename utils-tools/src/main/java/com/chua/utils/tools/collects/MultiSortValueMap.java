package com.chua.utils.tools.collects;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * 可重复值接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/17
 */
public interface MultiSortValueMap<K, V> {
    /**
     * 获取 entry
     *
     * @return entry
     */
    Set<Map.Entry<K, V>> entrySet();

    /**
     * 添加值
     *
     * @param key   索引
     * @param value 值
     * @return V
     */
    V put(K key, final V value);

    /**
     * 数量
     *
     * @return 数量
     */
    int size();

    /**
     * 是否为空
     *
     * @return 空返回true
     */
    boolean isEmpty();

    /**
     * 包含索引
     *
     * @param key 索引
     * @return 包含索引返回true
     */
    boolean containsKey(K key);

    /**
     * 包含值
     *
     * @param value 值
     * @return 包含值返回true
     */
    boolean containsValue(V value);

    /**
     * 获取数据
     *
     * @param key 索引
     * @return 数据
     */
    SortedSet<V> get(K key);

    /**
     * 移除数据
     *
     * @param key 索引
     * @return 移除的数据
     */
    SortedSet<V> remove(K key);

    /**
     * 添加数据
     *
     * @param key    索引
     * @param values 数据
     */
    void putAll(K key, SortedSet<V> values);

    /**
     * 清除数据
     */
    void clear();

    /**
     * 获取所有key
     *
     * @return keys
     */
    Set<K> keySet();

    /**
     * 获取所有数据集合
     *
     * @return V集合
     */
    Collection<V> values();

    /**
     * 获取所有索引的第一个值
     *
     * @return 所有索引的第一个值
     */
    Map<K, V> getAllFirst();
}

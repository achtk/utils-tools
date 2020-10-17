package com.chua.utils.tools.collects;

import java.io.Serializable;
import java.util.*;

/**
 * 可重复值接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/17
 */
public interface MultiMap<K, V> {
    /**
     * 数据长度
     *
     * @return
     */
    int size();

    /**
     * 数据长度
     *
     * @param k 索引
     * @return
     */
    int size(K k);

    /**
     * 设置数据
     *
     * @param key   索引
     * @param value 值
     * @return
     */
    V put(K key, V value);

    /**
     * 设置数据
     *
     * @param key   索引
     * @param values 值
     * @return
     */
    Set<V> put(K key, Set<V> values);
    /**
     * 设置值
     *
     * @param map 集合
     */
    void putAll(Map<? extends K, ? extends Set<V>> map);

    /**
     * 替换
     *
     * @param key   索引
     * @param value 值
     * @return
     */
    Set<V> replace(K key, Set<V> value);

    /**
     * 替换
     *
     * @param map 集合
     */
    void replaceAll(Map<? extends K, ? extends Set<V>> map);

    /**
     * 获取值
     *
     * @param key 索引
     * @return
     */
    Set<V> get(K key);

    /**
     * 获取索引
     *
     * @return
     */
    Set<K> keySet();

    /**
     * 获取所有值
     *
     * @return
     */
    Collection<Set<V>> values();

    /**
     * 获取所有值
     *
     * @param k 索引
     * @return
     */
    Collection<V> values(K k);

    /**
     * 获取数据对象
     *
     * @return
     */
    Set<Map.Entry<K, Set<V>>> entrySet();

    /**
     * 删除数据
     *
     * @param key 索引
     * @return
     */
    Set<V> remove(K key);

    /**
     * 清空
     */
    void clear();

    /**
     * 是否包含值
     *
     * @param value 值
     * @return
     */
    boolean containsValue(V value);

    /**
     * 是否包含索引
     *
     * @param key 索引
     * @return
     */
    boolean containsKey(K key);

    /**
     * 数据是否为空
     *
     * @return
     */
    default boolean isEmpty() {
        return 0 == size();
    }

    /**
     * 数据是否为空
     *
     * @param k 索引
     * @return
     */
    default boolean isEmpty(K k) {
        return 0 == size(k);
    }

}

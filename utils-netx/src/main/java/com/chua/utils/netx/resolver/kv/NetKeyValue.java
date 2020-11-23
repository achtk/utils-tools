package com.chua.utils.netx.resolver.kv;

import java.util.Set;

/**
 * key-value
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public interface NetKeyValue<K, V> {

    /**
     * 获取值
     *
     * @param key 索引
     * @return 值
     */
    V getValue(K key);

    /**
     * 获取值
     *
     * @param key 索引
     * @return 值
     */
    Long del(K key);

    /**
     * 索引是否存在
     *
     * @param key 索引
     * @return 存在返回true
     */
    boolean exist(K key);

    /**
     * 获取值
     *
     * @param key   索引
     * @param value 值
     * @return 值
     */
    V set(K key, V value);

    /**
     * 获取所有key
     *
     * @param key 索引
     * @return 所有key
     */
    Set<K> keys(K key);
}

package com.chua.utils.netx.resolver.kv;

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
    V del(K key);

    /**
     * 获取值
     *
     * @param key   索引
     * @param value 值
     * @return 值
     */
    V set(K key, V value);
}

package com.chua.utils.netx.function;

import java.util.List;
import java.util.Map;

/**
 * key-value接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
public interface RKv<K, V> {
    /**
     * 设置Key-Value
     *
     * @param key   索引
     * @param value 值
     * @throws Exception Exception
     */
    void set(K key, V value) throws Exception;

    /**
     * 获取值
     *
     * @param key 索引
     * @return V
     * @throws Exception Exception
     */
    V get(K key) throws Exception;

    /**
     * 增长
     *
     * @param key  索引
     * @param step 步长
     * @param ttl  ttl(秒)
     * @throws Exception Exception
     */
    void incr(K key, int step, int ttl) throws Exception;

    /**
     * 增长
     *
     * @param key  索引
     * @param step 步长
     * @throws Exception Exception
     */
    void incr(K key, int step) throws Exception;

    /**
     * 设置Key-Value
     *
     * @param map 索引-值
     * @throws Exception Exception
     */
    void set(Map<K, V> map) throws Exception;

    /**
     * 设置Key-Value
     *
     * @param map 索引-值
     * @param ttl ttl(秒)
     * @throws Exception Exception
     */
    void set(Map<K, V> map, int ttl) throws Exception;

    /**
     * 设置Key-Value
     *
     * @param key   索引
     * @param value 值
     * @param ttl   ttl(秒)
     * @throws Exception Exception
     */
    void set(K key, V value, int ttl) throws Exception;

    /**
     * 设置ttl
     *
     * @param key 索引
     * @param ttl ttl(秒)
     * @throws Exception Exception
     */
    void ttl(K key, int ttl) throws Exception;

    /**
     * 获取值
     *
     * @param keys 索引
     * @return Map
     * @throws Exception Exception
     */
    Map<K, V> get(List<K> keys) throws Exception;

    /**
     * 删除
     *
     * @param key 索引
     * @throws Exception Exception
     */
    void del(K key) throws Exception;

    /**
     * 删除前缀
     *
     * @param key_ 前缀
     * @throws Exception Exception
     */
    void delPrefix(String key_) throws Exception;

    /**
     * 返回所有通配值
     * @param key_ keys
     * @throws Exception Exception
     */
    List<V> keys(String key_) throws Exception;
    /**
     * 返回所有通配值
     * @param key_ keys
     * @throws Exception
     */
    List<K> getKeys(String key_) throws Exception;
}

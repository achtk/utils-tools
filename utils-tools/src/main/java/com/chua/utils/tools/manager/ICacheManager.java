package com.chua.utils.tools.manager;

/**
 * 缓存管理器
 * @author CH
 * @date 2020-09-30
 */
public interface ICacheManager<K, V> {
    /**
     * 保存缓存
     * @param key 索引
     * @param value 值
     * @return
     */
    V put(K key, V value);

    /**
     * 获取值
     * @param key 值
     * @return
     */
    V getValue(K key);

    /**
     * 删除指定缓存
     * @param key 索引
     */
    void remove(K key);

    /**
     * 清除缓存
     */
    void clear();
}

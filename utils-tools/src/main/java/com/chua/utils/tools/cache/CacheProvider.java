package com.chua.utils.tools.cache;

import com.chua.utils.tools.config.CacheProperties;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存接口
 *
 * @author CH
 */
public interface CacheProvider<K, V> {
    /**
     * 基础配置项
     *
     * @param cacheProperties 缓存配置
     * @return CacheProvider
     */
    CacheProvider configure(CacheProperties cacheProperties);

    /**
     * 是否存在缓存
     *
     * @param name 索引
     * @return boolean
     */
    boolean containsKey(K name);

    /**
     * 获取所有数据
     *
     * @return ConcurrentMap<K, T>
     */
    ConcurrentMap<K, V> asMap();

    /**
     * 获取缓存
     *
     * @param name 索引
     * @return T
     */
    V get(K name);

    /**
     * 保存缓存
     *
     * @param name  索引
     * @param value 值
     * @return T
     */
    V put(K name, V value);

    /**
     * 更新缓存
     *
     * @param name  索引
     * @param value 值
     * @return T
     */
    V update(K name, V value);

    /**
     * 删除缓存
     *
     * @param name 索引
     */
    void remove(K name);

    /**
     * 删除缓存
     *
     * @param name 索引
     */
    void remove(List<K> name);

    /**
     * 清空
     */
    void removeAll();

    /**
     * 缓存数量
     *
     * @return 缓存数量
     */
    long size();
}

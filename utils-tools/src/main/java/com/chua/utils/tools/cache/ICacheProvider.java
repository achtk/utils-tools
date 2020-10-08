package com.chua.utils.tools.cache;

import com.chua.utils.tools.config.CacheProperties;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存接口
 * @author CH
 */
public interface ICacheProvider<K, T> {
    /**
     * 基础配置项
     * @param cacheProperties
     * @return
     */
    ICacheProvider configure(CacheProperties cacheProperties);

    /**
     * 是否存在缓存
     * @param name 索引
     * @return
     */
    boolean container(String name);
    /**
     * 获取所有数据
     * @return
     */
    ConcurrentMap<K, T> asMap();
    /**
     * 获取缓存
     * @param name 索引
     * @return
     */
    T get(K name);
    /**
     * 保存缓存
     * @param name 索引
     * @param value 值
     */
    T put(K name, T value);
    /**
     * 更新缓存
     * @param name  索引
     * @param value 值
     */
    T update(K name, T value);

    /**
     * 删除缓存
     * @param name  索引
     */
    void remove(K... name);
    /**
     * 删除缓存
     * @param name 索引
     */
    void remove(List<K> name);

    /**
     * 清空
     */
    void removeAll();

    /**
     * 缓存数量
     * @return
     */
    long size();
}

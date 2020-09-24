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
    public ICacheProvider configure(CacheProperties cacheProperties);

    /**
     * 是否存在缓存
     * @param name 索引
     * @return
     */
    public boolean container(String name);
    /**
     * 获取所有数据
     * @return
     */
    public ConcurrentMap<K, T> asMap();
    /**
     * 获取缓存
     * @param name 索引
     * @return
     */
    public T get(K name);
    /**
     * 保存缓存
     * @param name 索引
     * @param value 值
     */
    public T put(K name, T value);
    /**
     * 更新缓存
     * @param name  索引
     * @param value 值
     */
    public T update(K name, T value);

    /**
     * 删除缓存
     * @param name  索引
     */
    public void remove(K... name);
    /**
     * 删除缓存
     * @param name 索引
     */
    public void remove(List<K> name);

    /**
     * 清空
     */
    public void removeAll();

    /**
     * 缓存数量
     * @return
     */
    public long size();
}

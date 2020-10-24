package com.chua.utils.tools.cache;

import com.chua.utils.tools.config.CacheProperties;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存接口
 *
 * @author CH
 */
public interface MultiCacheProvider<K, T> extends ICacheProvider<K, Set<T>> {
    /**
     * 缓存数据
     *
     * @param key    索引
     * @param values 数据
     */
    void add(K key, T values);
    /**
     * 缓存数据
     *
     * @param key    索引
     * @param values 数据
     */
    void putAll(K key, Collection<T> values);
    /**
     * 缓存数据
     *
     * @param key    索引
     * @param values 数据
     */
    void putAll(K key, T[] values);

    /**
     * 通过值获取key
     */
    Set<K> getKey(T value);
}

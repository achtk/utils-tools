package com.chua.utils.tools.cache;

import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.config.CacheProperties;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    CacheProvider<K, V> configure(CacheProperties cacheProperties);

    /**
     * 初始化
     *
     * @return map
     */
    default Map<K, V> initialValue() {
        return Collections.emptyMap();
    }

    /**
     * 是否存在缓存
     *
     * @param name 索引
     * @return boolean
     */
    boolean containsKey(K name);

    /**
     * 是否存在缓存
     *
     * @param anEnum 索引
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    default boolean containsKey(Enum<?> anEnum) {
        if (null == anEnum) {
            return false;
        }
        ConcurrentMap<K, V> kvConcurrentMap = asMap();
        if (null == kvConcurrentMap || kvConcurrentMap.isEmpty()) {
            return false;
        }

        K k = FinderHelper.firstElement(kvConcurrentMap.keySet());
        if (k instanceof String) {
            return containsKey((K) anEnum.name().toLowerCase());
        }
        return false;
    }

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
     * 获取缓存
     *
     * @param anEnum 索引
     * @return V
     */
    @SuppressWarnings("unchecked")
    default V get(Enum<?> anEnum) {
        if (null == anEnum) {
            return null;
        }
        ConcurrentMap<K, V> kvConcurrentMap = asMap();
        if (null == kvConcurrentMap || kvConcurrentMap.isEmpty()) {
            return null;
        }

        K k = FinderHelper.firstElement(kvConcurrentMap.keySet());
        if (k instanceof String) {
            return get((K) anEnum.name().toLowerCase());
        }
        return null;
    }

    /**
     * 保存缓存
     *
     * @param name  索引
     * @param value 值
     * @return T
     */
    V put(K name, V value);

    /**
     * 保存缓存
     *
     * @param params 缓存数据
     */
    default void putAll(Map<K, V> params) {
        if (MapOperableHelper.isEmpty(params)) {
            return;
        }
        for (Map.Entry<K, V> entry : params.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

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

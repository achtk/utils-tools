package com.chua.utils.tools.manager;

import com.chua.utils.tools.common.BooleanHelper;

import java.util.Collection;
import java.util.Collections;

/**
 * 缓存管理器
 *
 * @author CH
 * @date 2020-09-30
 */
public interface CacheManager<K, V> {
    /**
     * 保存缓存
     *
     * @param key   索引
     * @param value 值
     * @return
     */
    V put(K key, V value);

    /**
     * 保存缓存
     *
     * @param key    索引
     * @param values 值
     * @return
     */
    default Collection<V> put(K key, Collection<V> values) {
        if (null == key || !BooleanHelper.hasLength(values)) {
            return Collections.emptyList();
        }
        for (V value : values) {
            put(key, value);
        }
        return values;
    }

    /**
     * 保存缓存
     *
     * @param key    索引
     * @param values 值
     * @return
     */
    default V[] put(K key, V[] values) {
        if (null == key || !BooleanHelper.hasLength(values)) {
            return null;
        }

        for (V value : values) {
            put(key, value);
        }
        return values;
    }

    /**
     * 获取值
     *
     * @param key 值
     * @return
     */
    V getValue(K key);

    /**
     * 删除指定缓存
     *
     * @param key 索引
     */
    void remove(K key);

    /**
     * 清除缓存
     */
    void clear();
}

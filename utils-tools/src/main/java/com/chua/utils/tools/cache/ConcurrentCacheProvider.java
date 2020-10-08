package com.chua.utils.tools.cache;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.config.CacheProperties;
import com.chua.utils.tools.manager.ICacheManager;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ConcurrentMap方式缓存
 * @author CH
 * @date 2020-09-30
 */
public class ConcurrentCacheProvider<K, V> implements ICacheProvider<K, V>, ICacheManager<K, V> {

    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();

    @Override
    public ICacheProvider configure(CacheProperties cacheProperties) {
        return this;
    }

    @Override
    public boolean container(K name) {
        return cache.containsKey(name);
    }

    @Override
    public ConcurrentMap<K, V> asMap() {
        return cache;
    }

    @Override
    public V get(K name) {
        return cache.get(name);
    }

    @Override
    public V put(K name, V value) {
        cache.put(name, value);
        return value;
    }

    @Override
    public V getValue(K key) {
        return get(key);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        removeAll();
    }

    @Override
    public V update(K name, V value) {
        return put(name, value);
    }

    @Override
    public void remove(K... name) {
        if(!BooleanHelper.hasLength(name)) {
            return;
        }
        for (K k : name) {
            cache.remove(k);
        }
    }

    @Override
    public void remove(List<K> name) {
        if(!BooleanHelper.hasLength(name)) {
            return;
        }
        for (K k : name) {
            cache.remove(k);
        }
    }

    @Override
    public void removeAll() {
        cache.clear();
    }

    @Override
    public long size() {
        return cache.size();
    }
}

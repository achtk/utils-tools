package com.chua.utils.tools.cache;

import com.chua.utils.tools.config.CacheProperties;
import com.chua.utils.tools.manager.ICacheManager;
import com.google.common.collect.HashMultimap;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ConcurrentMap方式缓存
 * @author CH
 * @date 2020-09-30
 */
public class ConcurrentSetCacheProvider<K, V> implements ICacheProvider<K, Set<V>>, ICacheManager<K, Set<V>> {

    private final ThreadLocal<HashMultimap<K, V>> threadLocal = new ThreadLocal<HashMultimap<K, V>>() {
        @Override
        protected HashMultimap<K, V> initialValue() {
            HashMultimap<K, V> hashMultimap = HashMultimap.create();
            return hashMultimap;
        }
    };

    @Override
    public ICacheProvider configure(CacheProperties cacheProperties) {
        return this;
    }

    @Override
    public boolean container(K name) {
        return threadLocal.get().containsKey(name);
    }

    @Override
    public ConcurrentMap<K, Set<V>> asMap() {
        HashMultimap<K, V> hashMultimap = threadLocal.get();
        ConcurrentHashMap<K, Set<V>> concurrentHashMap = new ConcurrentHashMap<>();
        for (K key : hashMultimap.keys()) {
            concurrentHashMap.put(key, hashMultimap.get(key));
        }
        return concurrentHashMap;
    }

    @Override
    public Set<V> get(K name) {
        return threadLocal.get().get(name);
    }

    @Override
    public Set<V> put(K name, Set<V> value) {
        HashMultimap<K, V> kvHashMultimap = threadLocal.get();
        for (V v : value) {
            kvHashMultimap.put(name, v);
        }
        return value;
    }

    @Override
    public Set<V> update(K name, Set<V> value) {
        return put(name, value);
    }

    @Override
    public Set<V> getValue(K key) {
        return threadLocal.get().get(key);
    }

    @Override
    public void remove(K key) {
        threadLocal.get().removeAll(key);
    }

    @Override
    public void clear() {
        threadLocal.get().clear();
    }

    @Override
    public void remove(K... name) {
        for (K k : name) {
            remove(k);
        }
    }

    @Override
    public void remove(List<K> name) {
        for (K k : name) {
            remove(k);
        }
    }

    @Override
    public void removeAll() {
        clear();
    }

    @Override
    public long size() {
        return threadLocal.get().size();
    }

}

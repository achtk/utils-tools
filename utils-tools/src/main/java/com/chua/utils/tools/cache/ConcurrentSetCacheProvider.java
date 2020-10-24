package com.chua.utils.tools.cache;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.config.CacheProperties;
import com.chua.utils.tools.manager.ICacheManager;
import com.google.common.collect.HashMultimap;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ConcurrentMap方式缓存
 *
 * @author CH
 * @date 2020-09-30
 */
public class ConcurrentSetCacheProvider<K, V> implements MultiCacheProvider<K, V> {

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
        HashMultimap<K, V> kvHashMultimap = threadLocal.get();
        ConcurrentHashMap<K, Set<V>> concurrentHashMap = new ConcurrentHashMap<>();
        for (K key : kvHashMultimap.keys()) {
            concurrentHashMap.put(key, kvHashMultimap.get(key));
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
        threadLocal.get().clear();
    }

    @Override
    public long size() {
        return threadLocal.get().size();
    }


    @Override
    public void add(K key, V value) {
        if(null == key || null == value) {
            return;
        }
        threadLocal.get().put(key, value);
    }

    @Override
    public void putAll(K key, Collection<V> values) {
        if(null == key || !BooleanHelper.hasLength(values)) {
            return;
        }
        for (V value : values) {
            threadLocal.get().put(key, value);
        }
    }

    @Override
    public void putAll(K key, V[] values) {
        if(null == key || !BooleanHelper.hasLength(values)) {
            return;
        }
        for (V value : values) {
            threadLocal.get().put(key, value);
        }
    }

    @Override
    public Set<K> getKey(V value) {
        HashMultimap<K, V> kvHashMultimap = threadLocal.get();
        Set<K> keys = new HashSet<>();
        for (K k : kvHashMultimap.keySet()) {
            Set<V> vs = kvHashMultimap.get(k);
            if(!vs.contains(value)) {
                continue;
            }
            keys.add(k);
        }
        return keys;
    }
}

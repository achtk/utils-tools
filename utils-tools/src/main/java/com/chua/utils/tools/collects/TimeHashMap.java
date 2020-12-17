package com.chua.utils.tools.collects;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * 超时时间合集
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public class TimeHashMap<K, V> implements TimeMap<K, V> {

    private final Cache<K, V> cache;
    private BiConsumer<K, V> consumer;

    public TimeHashMap() {
        this(-1, TimeUnit.SECONDS);
    }

    public TimeHashMap(int duration) {
        this(duration, TimeUnit.SECONDS);
    }

    public TimeHashMap(int duration, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder().removalListener((RemovalListener<K, V>) notification -> {
            if (null != consumer) {
                consumer.accept(notification.getKey(), notification.getValue());
            }
        }).concurrencyLevel(16).expireAfterWrite(duration, timeUnit).build();
    }

    @Override
    public int size() {
        return Long.valueOf(cache.size()).intValue();
    }

    @Override
    public boolean isEmpty() {
        return cache.size() == 0L;
    }

    @Override
    public boolean containsKey(Object key) {
        ConcurrentMap<K, V> asMap = cache.asMap();
        return asMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        ConcurrentMap<K, V> asMap = cache.asMap();
        return asMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return cache.getIfPresent(key);
    }

    @Override
    public V put(K key, V value) {
        cache.put(key, value);
        return value;
    }

    @Override
    public V remove(Object key) {
        V v = get(key);
        if (null != v) {
            cache.invalidate(key);
        }
        return v;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        cache.putAll(m);
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }

    @Override
    public Set<K> keySet() {
        ConcurrentMap<K, V> concurrentMap = cache.asMap();
        return concurrentMap.keySet();
    }

    @Override
    public Collection<V> values() {
        ConcurrentMap<K, V> concurrentMap = cache.asMap();
        return concurrentMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        ConcurrentMap<K, V> concurrentMap = cache.asMap();
        return concurrentMap.entrySet();
    }

    @Override
    public void detector(BiConsumer<K, V> consumer) {
        this.consumer = consumer;
    }
}

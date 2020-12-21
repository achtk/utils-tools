package com.chua.utils.tools.collects;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 双向Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public class DuplexHashMap<K, V> implements DuplexMap<K, V> {

    private BiMap<K, V> map;

    public DuplexHashMap() {
        this.map = HashBiMap.create();
    }

    /**
     * 大小
     * @param expectedSize 大小
     */
    public DuplexHashMap(int expectedSize) {
        this.map = HashBiMap.create(expectedSize);
    }


    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsKey(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public K getInverse(V value) {
        return map.inverse().get(value);
    }
}

package com.chua.utils.tools.collects;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/17
 */
public class HashMultiMap<K, V>  implements MultiMap<K, V>, Cloneable, Serializable {

    private transient ConcurrentMap<K, Set<V>> temp = new ConcurrentHashMap<>();

    @Override
    public int size() {
        return temp.size();
    }

    @Override
    public boolean isEmpty() {
        return temp.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return temp.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return temp.containsValue(value);
    }

    @Override
    public Set<V> get(K key) {
        return temp.get(key);
    }

    @Override
    public int size(K k) {
        return containsKey(k) ? get(k).size() : 0;
    }

    @Override
    public V put(K key, V value) {
        if(null == key || null == value) {
            throw new NullPointerException();
        }
        if(!containsKey(key)) {
            Set<V> list = new HashSet<>();
            list.add(value);
            temp.put(key, list);
            return value;
        }
        Set<V> vs = get(key);
        vs.add(value);

        return value;
    }

    @Override
    public Set<V> put(K key, Set<V> values) {
        if(null == key || null == values) {
            throw new NullPointerException();
        }
        Set<V> vs = get(key);
        vs.addAll(values);
        return values;
    }

    @Override
    public void putAll(Map<? extends K, ? extends Set<V>> map) {
        if(null == map) {
            return;
        }
        for (Map.Entry<? extends K, ? extends Set<V>> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<V> replace(K key, Set<V> value) {
        temp.put(key, value);
        return value;
    }

    @Override
    public void replaceAll(Map<? extends K, ? extends Set<V>> map) {
        if(null == map) {
            return;
        }
        temp.putAll(map);
    }

    @Override
    public Set<K> keySet() {
        return temp.keySet();
    }

    @Override
    public Collection<Set<V>> values() {
        return temp.values();
    }

    @Override
    public Collection<V> values(K k) {
        return temp.get(k);
    }

    @Override
    public Set<Map.Entry<K, Set<V>>> entrySet() {
        return temp.entrySet();
    }

    @Override
    public  Set<V> remove(K key) {
        return temp.remove(key);
    }

    @Override
    public void clear() {
        temp.clear();
    }
}

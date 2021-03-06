package com.chua.utils.tools.collects;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

/**
 * 多值Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/17
 */
public class MultiValueSetMap<K, V> implements MultiSetValueMap<K, V>, Cloneable, Serializable {

    private transient final Map<K, Set<V>> targetMap = new HashMap<>();

    public static <V, K> MultiValueSetMap<K, V> create() {
        return new MultiValueSetMap<>();
    }

    @Override
    public int size() {
        return targetMap.size();
    }

    @Override
    public boolean isEmpty() {
        return targetMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return targetMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return targetMap.containsValue(value);
    }

    @Override
    public Set<V> get(Object key) {
        return targetMap.get(key);
    }

    @Override
    public Set<V> put(K key, Set<V> value) {
        return targetMap.put(key, value);
    }

    @Override
    public Set<V> remove(Object key) {
        return targetMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends Set<V>> m) {
        targetMap.putAll(m);
    }

    @Override
    public void clear() {
        targetMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return targetMap.keySet();
    }

    @Override
    public Collection<Set<V>> values() {
        return targetMap.values();
    }

    @Override
    public Set<Entry<K, Set<V>>> entrySet() {
        return targetMap.entrySet();
    }

    @Override
    public void add(K key, @Nullable V value) {
        Set<V> values = targetMap.computeIfAbsent(key, k -> new HashSet<>());
        values.add(value);
    }

    @Override
    public void addAll(K key, Set<? extends V> values) {
        Set<V> keyValues = targetMap.computeIfAbsent(key, k -> new HashSet<>());
        keyValues.addAll(values);
    }

    @Override
    public void addAll(MultiSetValueMap<K, V> values) {
        targetMap.putAll(values);
    }

    @Override
    public void set(K key, @Nullable V value) {
        Set<V> values = new HashSet<>();
        values.add(value);
        this.targetMap.put(key, values);
    }

    @Override
    public void setAll(Map<K, V> values) {
        for (Entry<K, V> entry : values.entrySet()) {
            this.set(entry.getKey(), entry.getValue());
        }
    }
}

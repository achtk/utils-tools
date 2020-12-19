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
public class MultiValueMap<K, V> implements MultiListValueMap<K, V>, Cloneable, Serializable {

    private final transient Map<K, List<V>> targetMap = new HashMap<>();

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
    public List<V> get(Object key) {
        return targetMap.get(key);
    }

    @Override
    public List<V> put(K key, List<V> value) {
        return targetMap.put(key, value);
    }

    @Override
    public List<V> remove(Object key) {
        return targetMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> m) {
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
    public Collection<List<V>> values() {
        return targetMap.values();
    }

    @Override
    public Set<Entry<K, List<V>>> entrySet() {
        return targetMap.entrySet();
    }

    @Override
    public void add(K key, @Nullable V value) {
        List<V> values = targetMap.computeIfAbsent(key, k -> new LinkedList<>());
        values.add(value);
    }

    @Override
    public void addAll(K key, List<? extends V> values) {
        List<V> keyValues = targetMap.computeIfAbsent(key, k -> new LinkedList<>());
        keyValues.addAll(values);
    }

    @Override
    public void addAll(MultiListValueMap<K, V> values) {
        targetMap.putAll(values);
    }

    @Override
    public void set(K key, @Nullable V value) {
        List<V> values = new LinkedList<>();
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

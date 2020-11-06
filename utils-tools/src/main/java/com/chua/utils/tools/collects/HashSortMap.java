package com.chua.utils.tools.collects;

import com.chua.utils.tools.classes.ClassHelper;
import com.google.common.collect.Ordering;
import com.google.common.collect.SortedMultiset;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class HashSortMap<K, V> extends TreeMap<K, List<V>> implements ListMultiValueSortMap<K, V>, Cloneable, Serializable {

    private Comparator comparator = new ValueComparator();


    public HashSortMap(Comparator<V> comparator) {
        this.comparator = comparator;
    }

    private final transient Map<K, List<V>> targetMap = new TreeMap<>();

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
        List<V> newValue = new ArrayList<>();
        List<V> vs = get(key);
        if (null != vs) {
            newValue.addAll(vs);
        }
        newValue.addAll(value);
        Collections.sort(newValue, comparator);
        targetMap.remove(key);
        return targetMap.put(key, newValue);
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
    public void add(K key, @Nullable V value) {
        this.put(key, Collections.singletonList(value));
    }

    @Override
    public void addAll(K key, List<? extends V> values) {
        List<V> newValues = new ArrayList<>();
        newValues.addAll(values);
        List<V> keyValues = targetMap.computeIfAbsent(key, k -> new LinkedList<>());
        newValues.addAll(keyValues);

        this.put(key, newValues);
    }

    @Override
    public void addAll(ListMultiValueSortMap<K, V> values) {
        for (Map.Entry<K, List<V>> entry : values.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void set(K key, @Nullable V value) {
        List<V> values = new LinkedList<>();
        values.add(value);
        this.targetMap.put(key, values);
    }

    @Override
    public void setAll(Map<K, V> values) {
        for (Map.Entry<K, V> entry : values.entrySet()) {
            this.set(entry.getKey(), entry.getValue());
        }
    }

    private class ValueComparator implements Comparator<Comparable<V>> {
        @Override
        public int compare(Comparable o1, Comparable o2) {
            return o1.compareTo(o2);
        }
    }
}

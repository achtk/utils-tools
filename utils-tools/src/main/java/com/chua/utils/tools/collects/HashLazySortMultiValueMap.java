package com.chua.utils.tools.collects;

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
public class HashLazySortMultiValueMap<K, V> extends TreeMap<K, List<V>> implements SortMultiValueMap<K, V>, Cloneable, Serializable {

    private Comparator comparator = new ValueComparator();


    public HashLazySortMultiValueMap(Comparator<V> comparator) {
        this.comparator = comparator;
    }

    private final transient ListMultiValueMap<Object, V> targetMap = new MultiValueMap<>();

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
        List<V> vs = targetMap.get(key);
        Collections.sort(vs, comparator);
        targetMap.put(key, vs);
        return vs;
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
        return (Set<K>) targetMap.keySet();
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
        this.targetMap.addAll(key, values);
    }

    @Override
    public void addAll(SortMultiValueMap<K, V> values) {
        for (Map.Entry<K, List<V>> entry : values.entrySet()) {
            this.addAll(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void set(K key, @Nullable V value) {
        this.targetMap.set(key, value);
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

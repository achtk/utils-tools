package com.chua.utils.tools.collects;

import com.chua.utils.tools.classes.ClassHelper;
import com.google.common.collect.Ordering;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 多值可排序
 * <p>实现方式主要采用{@link SortedSet}, {@link TreeSet}</p>
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public class MultiValueSortMap<K, V> implements MultiSortValueMap<K, V> {

    private final Map<K, SortedSet<V>> temporary = new HashMap<>();
    private final Comparator<V> comparator;
    int size = 0;

    public MultiValueSortMap(Comparator<V> comparator) {
        this.comparator = comparator;
    }

    public static <K, V> MultiSortValueMap<K, V> create(Comparator<V> comparator) {
        return new MultiValueSortMap<>(comparator);
    }

    public static <K, V> MultiSortValueMap<K, V> create(String comparatorValue) {
        return new MultiValueSortMap<>((o1, o2) -> {
            Object value1 = ClassHelper.getFieldIfOnlyValue(o1, comparatorValue);
            Object value2 = ClassHelper.getFieldIfOnlyValue(o2, comparatorValue);
            if (value1 instanceof Comparable && value2 instanceof Comparable) {
                return Ordering.natural().compare((Comparable) value1, (Comparable) value2);
            }
            return Ordering.natural().compare(1, 1);
        });
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(K key) {
        return temporary.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        Collection<SortedSet<V>> values = temporary.values();
        for (SortedSet<V> item : values) {
            if (item.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SortedSet<V> get(K key) {
        return temporary.get(key);
    }


    @Override
    public SortedSet<V> remove(K key) {
        return temporary.remove(key);
    }

    @Override
    public void putAll(K key, SortedSet<V> values) {
        temporary.computeIfPresent(key, (k, v) -> new TreeSet<>(comparator)).addAll(values);
    }

    @Override
    public void clear() {
        temporary.clear();
    }

    @Override
    public Set<K> keySet() {
        return temporary.keySet();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> result = new HashSet<>();
        temporary.entrySet().stream().forEach(entry -> {
            SortedSet<V> entryValue = entry.getValue();
            result.addAll(entryValue.stream().map(el -> new Map.Entry<K, V>() {
                @Override
                public K getKey() {
                    return entry.getKey();
                }

                @Override
                public V getValue() {
                    return el;
                }

                @Override
                public V setValue(V value) {
                    return value;
                }
            }).collect(Collectors.toSet()));
        });
        return result;
    }

    @Override
    public V put(K key, V value) {
        SortedSet<V> sortedSet = temporary.get(key);
        if(null == sortedSet) {
            sortedSet = new TreeSet<>(comparator);
        }
        sortedSet.add(value);
        temporary.put(key, sortedSet);
        return value;
    }

    @Override
    public Collection<V> values() {
        List<V> result = new ArrayList<>();
        temporary.entrySet().stream().forEach(entry -> {
            result.addAll(entry.getValue());
        });
        return result;
    }

    @Override
    public Map<K, V> getAllFirst() {
        Set<K> kSet = temporary.keySet();
        Map<K, V> result = new HashMap<>(kSet.size());
        for (K k : kSet) {
            SortedSet<V> sortedSet = temporary.get(k);
            result.put(k, sortedSet.first());
        }
        return result;
    }
}

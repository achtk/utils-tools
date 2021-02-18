package com.chua.utils.tools.collects;

import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * 区间Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public class RangeHashMap<K extends Comparable, V> implements RangeMap<K, V> {

    private final com.google.common.collect.RangeMap<K, V> rangeMap;

    public RangeHashMap() {
        this.rangeMap = TreeRangeMap.create();
    }

    @Nullable
    @Override
    public V get(K key) {
        return rangeMap.get(key);
    }

    @Override
    public Map.@Nullable Entry<Range<K>, V> getEntry(K key) {
        return rangeMap.getEntry(key);
    }

    @Override
    public Range<K> span() {
        return rangeMap.span();
    }

    @Override
    public void put(Range<K> range, V value) {
        rangeMap.put(range, value);
    }

    @Override
    public void putCoalescing(Range<K> range, V value) {
        rangeMap.putCoalescing(range, value);
    }

    @Override
    public void putAll(com.google.common.collect.RangeMap<K, V> rangeMap) {
        rangeMap.putAll(rangeMap);
    }

    @Override
    public void clear() {
        rangeMap.clear();
    }

    @Override
    public void remove(Range<K> range) {
        rangeMap.remove(range);
    }

    @Override
    public void merge(Range<K> range, @Nullable V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        rangeMap.merge(range, value, remappingFunction);
    }

    @Override
    public Map<Range<K>, V> asMapOfRanges() {
        return rangeMap.asMapOfRanges();
    }

    @Override
    public Map<Range<K>, V> asDescendingMapOfRanges() {
        return rangeMap.asDescendingMapOfRanges();
    }

    @Override
    public com.google.common.collect.RangeMap<K, V> subRangeMap(Range<K> range) {
        return rangeMap.subRangeMap(range);
    }

    @Override
    public void putCloseOpen(K left, K right, V value) {
        Range<K> range = Range.closedOpen(left, right);
        put(range, value);
    }

    @Override
    public void putClose(K left, K right, V value) {
        Range<K> range = Range.closed(left, right);
        put(range, value);
    }

    @Override
    public void putOpen(K left, K right, V value) {
        Range<K> range = Range.open(left, right);
        put(range, value);
    }

    @Override
    public void putOpenClose(K left, K right, V value) {
        Range<K> range = Range.openClosed(left, right);
        put(range, value);
    }
}

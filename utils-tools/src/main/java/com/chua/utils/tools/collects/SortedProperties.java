package com.chua.utils.tools.collects;

import java.util.*;

/**
 * 排序 Properties<br />
 * 部分参考Spring
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/28
 */
public class SortedProperties extends Properties {

    private Comparator<Object> keyComparator = Comparator.comparing(String::valueOf);

    private Comparator<Map.Entry<Object, Object>> entryComparator = Map.Entry.comparingByKey(keyComparator);

    public SortedProperties() {
    }

    public SortedProperties(Comparator<Object> keyComparator) {
        this.keyComparator = keyComparator;
        this.entryComparator = Map.Entry.comparingByKey(keyComparator);
    }

    /**
     * Return a sorted enumeration of the keys in this {@link Properties} object.
     * @see #keySet()
     */
    @Override
    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(keySet());
    }


    @Override
    public Set<Object> keySet() {
        Set<Object> sortedKeys = new TreeSet<>(keyComparator);
        sortedKeys.addAll(super.keySet());
        return Collections.synchronizedSet(sortedKeys);
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        Set<Map.Entry<Object, Object>> sortedEntries = new TreeSet<>(entryComparator);
        sortedEntries.addAll(super.entrySet());
        return Collections.synchronizedSet(sortedEntries);
    }
}

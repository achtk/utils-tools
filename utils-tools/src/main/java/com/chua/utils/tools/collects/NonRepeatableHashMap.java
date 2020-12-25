package com.chua.utils.tools.collects;

import java.util.HashMap;
import java.util.Map;

/**
 * 不可重复录入的HashMap
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
public class NonRepeatableHashMap<K, V> extends HashMap<K, V> {

    @Override
    public V put(K key, V value) {
        V v = get(key);
        if(null == v) {
            return super.put(key, value);
        }
        return v;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().forEach(entry -> {
            this.put(entry.getKey(), entry.getValue());
        });
    }
}

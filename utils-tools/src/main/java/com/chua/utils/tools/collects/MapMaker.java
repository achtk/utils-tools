package com.chua.utils.tools.collects;

import com.chua.utils.tools.classes.ClassHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * Map创建者
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/21
 */
@RequiredArgsConstructor(staticName = "of")
public class MapMaker<K, V> {

    @NonNull
    private final Class<? extends Map> mapClass;
    /**
     * 初始化大小
     */
    private int capacity = 1 << 4;
    /**
     * 索引类型
     */
    private Class<? extends K> keyClass;
    /**
     * 索引类型
     */
    private Class<? extends V> valueClass;
    /**
     * 数据
     */
    private final Map<K, V> loaderMapData = new HashMap<>();

    /**
     * 初始化大小
     *
     * @param capacity 初始化大小
     * @return this
     */
    public MapMaker withCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    /**
     * 索引类型
     *
     * @param keyClass 索引类型
     * @return this
     */
    public MapMaker withKeyClass(Class<? extends K> keyClass) {
        this.keyClass = keyClass;
        return this;
    }

    /**
     * 索引类型
     *
     * @param valueClass 索引类型
     * @return this
     */
    public MapMaker withValueClass(Class<? extends V> valueClass) {
        this.valueClass = valueClass;
        return this;
    }

    /**
     * 回调
     *
     * @param supplier 回调
     * @return this
     */
    public MapMaker withLoader(Supplier<Map<? extends K, ? extends V>> supplier) {
        Map<? extends K, ? extends V> map = supplier.get();
        if (null != map && !map.isEmpty()) {
            loaderMapData.putAll(map);
        }
        return this;
    }

    /**
     * 回调
     *
     * @param key   索引
     * @param value 值
     * @return this
     */
    public MapMaker withLoader(K key, V value) {
        if (null != key) {
            loaderMapData.put(key, value);
        }
        return this;
    }

    /**
     * 创建Map
     *
     * @return Map
     */
    public Map<K, V> create() {
        Map<K, V> result;
        if (ConcurrentMap.class.isAssignableFrom(mapClass)) {
            result = new ConcurrentHashMap<>(capacity);
        } else if (LinkedHashMap.class.isAssignableFrom(mapClass)) {
            result = new LinkedHashMap<>(capacity);
        } else if (HashMap.class.isAssignableFrom(mapClass)) {
            result = new HashMap<>(capacity);
        } else if (DuplexMap.class.isAssignableFrom(mapClass)) {
            result = new DuplexHashMap<>(capacity);
        } else if (TimeMap.class.isAssignableFrom(mapClass)) {
            result = new TimeHashMap<>();
        } else {
            result = ClassHelper.forObject(mapClass);
        }
        if (null == result) {
            return null;
        }
        result.putAll(Optional.ofNullable(loaderMapData).orElse(Collections.emptyMap()));

        return result;
    }
}

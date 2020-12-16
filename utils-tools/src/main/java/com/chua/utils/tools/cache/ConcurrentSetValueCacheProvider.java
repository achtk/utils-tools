package com.chua.utils.tools.cache;

import com.chua.utils.tools.collects.MultiValueSetMap;
import com.chua.utils.tools.collects.SetMultiValueMap;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.config.CacheProperties;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;

/**
 * ConcurrentMap方式缓存
 *
 * @author CH
 * @date 2020-09-30
 */
public class ConcurrentSetValueCacheProvider<K, V> implements MultiValueCacheProvider<K, V> {

    private final SetMultiValueMap<K, V> threadLocal = MultiValueSetMap.create();

    public SetMultiValueMap<K, V> get() {
        return threadLocal;
    }

    @Override
    public CacheProvider configure(CacheProperties cacheProperties) {
        return this;
    }

    @Override
    public boolean containsKey(K name) {
        return get().containsKey(name);
    }

    @Override
    public ConcurrentMap<K, Set<V>> asMap() {
        ConcurrentMap<K, Set<V>> targetMap = new ConcurrentHashMap<>(DEFAULT_INITIAL_CAPACITY);
        for (Map.Entry<K, Set<V>> entry : threadLocal.entrySet()) {
            targetMap.put(entry.getKey(), entry.getValue());
        }
        return targetMap;
    }

    @Override
    public Set<V> get(K name) {
        return threadLocal.get(name);
    }

    @Override
    public Set<V> put(K name, Set<V> value) {
        return threadLocal.put(name, value);
    }

    @Override
    public Set<V> update(K name, Set<V> value) {
        return threadLocal.put(name, value);
    }

    @Override
    public void remove(K name) {
        threadLocal.remove(name);
    }

    @Override
    public void remove(List<K> name) {
        for (K k : name) {
            remove(k);
        }
    }

    @Override
    public void removeAll() {
        threadLocal.clear();
    }

    @Override
    public long size() {
        return threadLocal.size();
    }


    @Override
    public V getIndex(K k, int index) {
        return FinderHelper.findElement(index, threadLocal.get(k));
    }

    @Override
    public void add(K keyValue, @Nullable V value) {
        threadLocal.computeIfAbsent(keyValue, k -> new HashSet<>()).add(value);
    }

    @Override
    public void addAll(K key, Set<? extends V> values) {
        threadLocal.computeIfAbsent(key, k -> new HashSet<>()).addAll(values);
    }


}

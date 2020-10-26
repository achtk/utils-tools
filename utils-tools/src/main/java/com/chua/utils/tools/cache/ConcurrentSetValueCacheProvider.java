package com.chua.utils.tools.cache;

import com.chua.utils.tools.collects.HashSetMultiValueMap;
import com.chua.utils.tools.collects.SetMultiValueMap;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.config.CacheProperties;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * ConcurrentMap方式缓存
 *
 * @author CH
 * @date 2020-09-30
 */
public class ConcurrentSetValueCacheProvider<K, V> implements MultiValueCacheProvider<K, V> {

    private final ThreadLocal<SetMultiValueMap<K, V>> threadLocal = new ThreadLocal<SetMultiValueMap<K, V>>() {
        @Override
        protected SetMultiValueMap<K, V> initialValue() {
            HashSetMultiValueMap<K, V> valueMap = HashSetMultiValueMap.create();
            return valueMap;
        }
    };

    public SetMultiValueMap<K, V> get() {
        return threadLocal.get();
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
        return null;
    }

    @Override
    public Set<V> get(K name) {
        return get(name);
    }

    @Override
    public Set<V> put(K name, Set<V> value) {
        return get().put(name, value);
    }

    @Override
    public Set<V> update(K name, Set<V> value) {
        return get().put(name, value);
    }

    @Override
    public void remove(K name) {
        get().remove(name);
    }

    @Override
    public void remove(List<K> name) {
        for (K k : name) {
            remove(k);
        }
    }

    @Override
    public void removeAll() {
        get().clear();
    }

    @Override
    public long size() {
        return get().size();
    }


    @Override
    public V getIndex(K k, int index) {
        return FinderHelper.findElement(index, get().get(k));
    }
}

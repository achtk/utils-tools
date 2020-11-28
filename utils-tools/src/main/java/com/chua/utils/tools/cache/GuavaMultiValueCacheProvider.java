package com.chua.utils.tools.cache;

import com.chua.utils.tools.action.ActionListener;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.config.CacheProperties;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * guava缓存
 *
 * @author CH
 */
public class GuavaMultiValueCacheProvider<K, T> implements MultiValueCacheProvider<K, T> {

    @Getter
    private Cache<K, Set<T>> cache;

    public GuavaMultiValueCacheProvider() {
        this.configure(new CacheProperties());
        this.putAll(initialValue());
    }

    @Override
    public CacheProvider configure(CacheProperties cacheProperties) {
        CacheBuilder<Object, Object> objectObjectCacheBuilder = CacheBuilder.newBuilder();

        if (cacheProperties.getMemMaximumSize() > 0) {
            objectObjectCacheBuilder.maximumSize(cacheProperties.getMemMaximumSize());
        }
        if (cacheProperties.getExpire() > 0) {
            objectObjectCacheBuilder.expireAfterAccess(cacheProperties.getExpire(), TimeUnit.SECONDS);
        }
        if (cacheProperties.getWriteExpire() > 0) {
            objectObjectCacheBuilder.expireAfterWrite(cacheProperties.getWriteExpire(), TimeUnit.SECONDS);
        }

        ActionListener removeListener = cacheProperties.getRemoveListener();
        if (null != removeListener) {
            objectObjectCacheBuilder.removalListener(notification -> removeListener.listener(notification.getKey(), notification.getValue(), notification.getCause()));
        }

        ActionListener updateListener = cacheProperties.getUpdateListener();
        if (null != updateListener) {
            final CacheLoader cacheLoader = new CacheLoader() {
                @Override
                public Object load(Object key) throws Exception {
                    updateListener.listener(key);
                    return key;
                }
            };
            cache = objectObjectCacheBuilder.build(cacheLoader);
        } else {
            cache = objectObjectCacheBuilder.build();
        }
        this.putAll(initialValue());
        return this;
    }

    @Override
    public boolean containsKey(K name) {
        return null != name && null != cache && null != cache.getIfPresent(name);
    }

    @Override
    public ConcurrentMap<K, Set<T>> asMap() {
        return cache.asMap();
    }

    @Override
    public Set<T> get(K name) {
        return cache.getIfPresent(name);
    }

    @Override
    public Set<T> put(K name, Set<T> value) {
        cache.put(name, value);
        return value;
    }

    @Override
    public Set<T> update(K name, Set<T> value) {
        cache.put(name, value);
        return value;
    }

    @Override
    public void remove(K name) {
        cache.invalidate(name);
    }

    @Override
    public void remove(List<K> name) {
        if (!BooleanHelper.hasLength(name)) {
            return;
        }
        for (K k : name) {
            cache.invalidate(k);
        }
    }

    @Override
    public void removeAll() {
        cache.invalidateAll();
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public T getIndex(K k, int index) {
        Set<T> ifPresent = cache.getIfPresent(k);
        return FinderHelper.findElement(index, ifPresent);
    }
}

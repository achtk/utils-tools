package com.chua.utils.tools.cache.caffeine;

import com.chua.utils.tools.action.ActionListener;
import com.chua.utils.tools.cache.ICacheProvider;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.config.CacheProperties;
import com.github.benmanes.caffeine.cache.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine 缓存
 * @author CHTK
 */
public class CaffeineProvider<K, T> implements ICacheProvider<K, T> {

    private Cache<Object, Object> cache;

    @Override
    public ICacheProvider configure(CacheProperties cacheProperties) {
        Caffeine<Object, Object> objectObjectCaffeine = Caffeine.newBuilder();
        if(cacheProperties.getMemMaximumSize() > 0) {
            objectObjectCaffeine.maximumSize(cacheProperties.getMemMaximumSize());
        }
        if(cacheProperties.getExpire() > 0) {
            objectObjectCaffeine.expireAfterAccess(cacheProperties.getExpire(), TimeUnit.SECONDS);
        }
        if(cacheProperties.getWriteExpire() > 0L) {
            objectObjectCaffeine.expireAfterWrite(cacheProperties.getWriteExpire(), TimeUnit.SECONDS);
        }

        objectObjectCaffeine.recordStats();

        ActionListener removeListener = cacheProperties.getRemoveListener();
        if(null != removeListener) {
            objectObjectCaffeine.removalListener(new RemovalListener<Object, Object>() {
                @Override
                public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause cause) {
                    removeListener.listener(key, value, cause);
                }
            });
        }

        ActionListener updateListener = cacheProperties.getUpdateListener();
        if (null != updateListener) {
            cache = objectObjectCaffeine.build(new CacheLoader<Object, Object>() {

                @Override
                public @Nullable Object load(@NonNull Object key) throws Exception {
                    updateListener.listener(key);
                    return key;
                }
            });
        }

        return this;
    }

    @Override
    public boolean container(K name) {
        return null == name ? false : cache.getIfPresent(name) != null;
    }

    @Override
    public ConcurrentMap<K, T> asMap() {
        return (ConcurrentMap<K, T>) cache.asMap();
    }

    @Override
    public T get(K name) {
        return null == name ? null : (T) cache.getIfPresent(name);
    }

    @Override
    public T put(K name, T value) {
        if(null == name) {
            return null;
        }
        cache.put(name, value);
        return value;
    }

    @Override
    public T update(K name, T value) {
        return put(name, value);
    }

    @Override
    public void remove(K... name) {
        if(!BooleanHelper.hasLength(name)) {
            return;
        }
        for (K k : name) {
            cache.invalidate(k);
        }
    }

    @Override
    public void remove(List<K> name) {
        if(!BooleanHelper.hasLength(name)) {
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
        return cache.asMap().size();
    }
}

package com.chua.utils.tools.cache;

import com.chua.utils.tools.action.ActionListener;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.config.CacheProperties;
import com.chua.utils.tools.manager.ICacheManager;
import com.google.common.cache.*;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * guava缓存
 * @author CH
 */
public class GuavaCacheProvider<K, T> implements ICacheProvider<K, T>, ICacheManager<K, T> {

    @Getter
    private Cache<K, T> cache;

    public GuavaCacheProvider() {
        this.configure(new CacheProperties());
    }

    @Override
    public ICacheProvider configure(CacheProperties cacheProperties) {
        CacheBuilder<Object, Object> objectObjectCacheBuilder = CacheBuilder.newBuilder();

        if(cacheProperties.getMemMaximumSize() > 0) {
            objectObjectCacheBuilder.maximumSize(cacheProperties.getMemMaximumSize());
        }
        if(cacheProperties.getExpire() > 0) {
            objectObjectCacheBuilder.expireAfterAccess(cacheProperties.getExpire(), TimeUnit.SECONDS);
        }
        if(cacheProperties.getWriteExpire() > 0) {
            objectObjectCacheBuilder.expireAfterWrite(cacheProperties.getWriteExpire(), TimeUnit.SECONDS);
        }

        ActionListener removeListener = cacheProperties.getRemoveListener();
        if(null != removeListener) {
            objectObjectCacheBuilder.removalListener(new RemovalListener<Object, Object>() {
                @Override
                public void onRemoval(RemovalNotification<Object, Object> notification) {
                    removeListener.listener(notification.getKey(), notification.getValue(), notification.getCause());
                }
            });
        }

        ActionListener updateListener = cacheProperties.getUpdateListener();
        if(null != updateListener) {
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

        return this;
    }

    @Override
    public boolean container(String name) {
        return null != name && null != cache && null != cache.getIfPresent(name);
    }

    @Override
    public ConcurrentMap<K, T> asMap() {
        return cache.asMap();
    }

    @Override
    public T get(K name) {
        return null == name ? null : cache.getIfPresent(name);
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
    public T getValue(K key) {
        return get(key);
    }

    @Override
    public void remove(K key) {
        remove(key);
    }

    @Override
    public void clear() {
        removeAll();
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
        return cache.size();
    }

}

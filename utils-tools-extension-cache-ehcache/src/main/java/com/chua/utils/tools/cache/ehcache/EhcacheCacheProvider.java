package com.chua.utils.tools.cache.ehcache;

import com.chua.utils.tools.cache.ICacheProvider;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.SizeHelper;
import com.chua.utils.tools.config.CacheProperties;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.*;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.EventType;
import org.ehcache.impl.events.CacheEventAdapter;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ehcache缓存
 * @author CH
 */
public class EhcacheCacheProvider<K, T> implements ICacheProvider<K, T> {


    private static final Class<?> VALUE_CLASS = Object.class;
    private static final Class<?> KEY_CLASS = Object.class;
    private CacheManager cacheManager;
    private Cache<Object, Object> cache;

    @Override
    public ICacheProvider configure(CacheProperties cacheProperties) {
        CacheConfigurationBuilder builder =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(KEY_CLASS, VALUE_CLASS,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .heap(10, MemoryUnit.MB)
                                .offheap(cacheProperties.getMemMaximumSize(), MemoryUnit.MB)
                                .disk(cacheProperties.getDiskMaximumSize(), MemoryUnit.MB, true)

                ).withDiskStoreThreadPool("persistenceThread", 5);

        if(cacheProperties.getExpire() > 0L) {
            builder.withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(cacheProperties.getExpire())));
        } else {
            builder.withExpiry(ExpiryPolicyBuilder.noExpiration());
        }
        //可缓存的最大对象大小
        //builder.withSizeOfMaxObjectSize(cacheConfig.getDiskMaximumSize(), MemoryUnit.MB);
        // 添加监听器
        builder.add(CacheEventListenerConfigurationBuilder.newEventListenerConfiguration(
                new CacheEventAdapter() {

                }, EventType.EXPIRED).unordered().asynchronous());

        builder.withDefaultClassLoader()
                .withDefaultKeySerializer()
                .withDefaultValueSerializer();

        this.cacheManager = CacheManagerBuilder.persistence(cacheProperties.getDir())
                .builder(CacheManagerBuilder.newCacheManagerBuilder()).build(true);


        cache = this.cacheManager.createCache(cacheProperties.getName(), builder);
        return this;
    }

    @Override
    public boolean container(String name) {
        return null == name ? false : cache.containsKey(name);
    }

    @Override
    public ConcurrentMap<K, T> asMap() {
        ConcurrentHashMap<K, T> concurrentHashMap = new ConcurrentHashMap<K, T>();
        Iterator<Cache.Entry<Object, Object>> iterator = cache.iterator();
        if(!BooleanHelper.hasLength(iterator)) {
            return null;
        }
        while (iterator.hasNext()) {
            Cache.Entry next = iterator.next();
            concurrentHashMap.put((K) next.getKey(), (T) next.getValue());
        }
        return concurrentHashMap;
    }

    @Override
    public T get(K name) {
        return null == name ? null : (T) cache.get(name);
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
            cache.remove(k);
        }
    }

    @Override
    public void remove(List<K> name) {
        if(!BooleanHelper.hasLength(name)) {
            return;
        }
        for (K k : name) {
            cache.remove(k);
        }
    }

    @Override
    public void removeAll() {
        Iterator<Cache.Entry<Object, Object>> iterator = cache.iterator();
        while (iterator.hasNext()) {
            cache.remove(iterator.next());
        }
    }

    @Override
    public long size() {
        return SizeHelper.size(cache.iterator());
    }
}

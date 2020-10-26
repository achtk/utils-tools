package com.chua.utils.tools.cache.ehcache;

import com.chua.utils.tools.cache.CacheProvider;
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
 *
 * @author CH
 */
public class EhcacheCacheProvider implements CacheProvider<Object, Object> {


    private static final Class<?> VALUE_CLASS = Object.class;
    private static final Class<?> KEY_CLASS = Object.class;
    private CacheManager cacheManager;
    private Cache<Object, Object> cache;

    @Override
    public CacheProvider<Object, Object> configure(CacheProperties cacheProperties) {
        CacheConfigurationBuilder builder =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(KEY_CLASS, VALUE_CLASS,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .heap(10, MemoryUnit.MB)
                                .offheap(cacheProperties.getMemMaximumSize(), MemoryUnit.MB)
                                .disk(cacheProperties.getDiskMaximumSize(), MemoryUnit.MB, true)

                ).withDiskStoreThreadPool("persistenceThread", 5);

        if (cacheProperties.getExpire() > 0L) {
            builder.withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(cacheProperties.getExpire())));
        } else {
            builder.withExpiry(ExpiryPolicyBuilder.noExpiration());
        }
        //可缓存的最大对象大小
        //builder.withSizeOfMaxObjectSize(cacheConfig.getDiskMaximumSize(), MemoryUnit.MB);
        //添加监听器
        builder.withService(CacheEventListenerConfigurationBuilder.newEventListenerConfiguration(
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
    public boolean containsKey(Object name) {
        return null != name && cache.containsKey(name);
    }

    @Override
    public ConcurrentMap<Object, Object> asMap() {
        Iterator<Cache.Entry<Object, Object>> iterator = cache.iterator();
        ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<>();
        if (!BooleanHelper.hasLength(iterator)) {
            return null;
        }
        while (iterator.hasNext()) {
            Cache.Entry<Object, Object> next = iterator.next();
            concurrentHashMap.put(next.getKey(), next.getValue());
        }
        return concurrentHashMap;
    }

    @Override
    public Object get(Object name) {
        return null == name ? null : cache.get(name);
    }

    @Override
    public Object put(Object name, Object value) {
        if (null == name) {
            return null;
        }
        cache.put(name, value);
        return value;
    }

    @Override
    public Object update(Object name, Object value) {
        return put(name, value);
    }

    @Override
    public void remove(Object name) {
        cache.remove(name);
    }

    @Override
    public void remove(List<Object> name) {
        if (!BooleanHelper.hasLength(name)) {
            return;
        }
        for (Object item : name) {
            cache.remove(item);
        }
    }

    @Override
    public void removeAll() {
        cache.clear();
    }

    @Override
    public long size() {
        return SizeHelper.getSize(cache.iterator());
    }
}

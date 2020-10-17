package com.chua.utils.tools.cache;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.PropertiesHelper;
import com.chua.utils.tools.config.CacheProperties;
import com.chua.utils.tools.manager.ICacheManager;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ConcurrentMap方式缓存
 * @author CH
 * @date 2020-09-30
 */
@NoArgsConstructor
public class ConcurrentCacheProvider<K, V> implements ICacheProvider<K, V>, ICacheManager<K, V> {

    private final ConcurrentHashMap<K, Properties> cache = new ConcurrentHashMap<>();
    private int timeout = -1;
    private static final String DEFAULT_TIMEOUT_FIELDS = "config.cache.timeout";
    private static final String DEFAULT_KEY_FIELDS = "config.cache.key";

    public ConcurrentCacheProvider(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public ICacheProvider configure(CacheProperties cacheProperties) {
        return this;
    }

    @Override
    public boolean container(K name) {
        return cache.containsKey(name);
    }

    @Override
    public ConcurrentMap<K, V> asMap() {
        ConcurrentHashMap<K, V> temp = new ConcurrentHashMap<>();
        for (K k : cache.keySet()) {
            V v = get(k);
            if(v == null) {
                continue;
            }
            temp.put(k, v);
        }
        return temp;
    }

    @Override
    public V get(K name) {
        Properties properties = cache.get(name);
        long ints = PropertiesHelper.longs(properties, DEFAULT_TIMEOUT_FIELDS);
        V value = (V) properties.get(DEFAULT_KEY_FIELDS);
        if(-1L == ints) {
            return value;
        }
        if(System.currentTimeMillis() - ints > timeout) {
            remove(name);
            return null;
        }
        return value;
    }

    @Override
    public V put(K name, V value) {
        Properties properties = new Properties();
        properties.put(DEFAULT_KEY_FIELDS, value);
        properties.put(DEFAULT_TIMEOUT_FIELDS, System.currentTimeMillis());
        cache.put(name, properties);
        return value;
    }

    @Override
    public V getValue(K key) {
        return get(key);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        removeAll();
    }

    @Override
    public V update(K name, V value) {
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
        cache.clear();
    }

    @Override
    public long size() {
        return cache.size();
    }
}

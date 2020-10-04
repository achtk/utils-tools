package com.chua.utils.tools.limit;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author CH
 */
public class TokenLimitProvider implements ILimiterProvider {

    private static final ConcurrentHashMap<String, RateLimiter> RATE_LIMITER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    private static final int DEFAULT_SIZE = 10;
    @Override
    public ILimiterProvider newLimiter(String name, int size) {
        RATE_LIMITER_CONCURRENT_HASH_MAP.put(name, RateLimiter.create(size));
        return this;
    }

    @Override
    public ILimiterProvider newLimiter(Map<String, Integer> config) {
        for (Map.Entry<String, Integer> entry : config.entrySet()) {
            RATE_LIMITER_CONCURRENT_HASH_MAP.put(entry.getKey(), RateLimiter.create(entry.getValue()));
        }
        return this;
    }

    @Override
    public boolean tryAcquire(String name) {
        RateLimiter rateLimiter = RATE_LIMITER_CONCURRENT_HASH_MAP.get(name);
        if(null == rateLimiter) {
            newLimiter(name, DEFAULT_SIZE);
            return tryAcquire(name);
        }
        return rateLimiter.tryAcquire();
    }

    @Override
    public boolean tryAcquire(String name, long time) {
        RateLimiter rateLimiter = RATE_LIMITER_CONCURRENT_HASH_MAP.get(name);
        if(null == rateLimiter) {
            newLimiter(name, DEFAULT_SIZE);
            return tryAcquire(name);
        }
        return rateLimiter.tryAcquire(time, TimeUnit.MICROSECONDS);
    }

    @Override
    public boolean tryAcquire(String name, long time, TimeUnit timeUnit) {
        RateLimiter rateLimiter = RATE_LIMITER_CONCURRENT_HASH_MAP.get(name);
        if(null == rateLimiter) {
            newLimiter(name, DEFAULT_SIZE);
            return tryAcquire(name);
        }
        return rateLimiter.tryAcquire(time, timeUnit);
    }

    @Override
    public boolean tryGet() {
        return false;
    }

    @Override
    public boolean containGroup(String group) {
        return RATE_LIMITER_CONCURRENT_HASH_MAP.containsKey(group);
    }
}

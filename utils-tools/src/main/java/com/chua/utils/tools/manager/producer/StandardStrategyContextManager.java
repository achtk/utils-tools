package com.chua.utils.tools.manager.producer;

import com.chua.utils.tools.function.able.InitializingCacheable;
import com.chua.utils.tools.manager.StrategyContextManager;
import com.chua.utils.tools.manager.builder.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 标准策略管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("all")
public class StandardStrategyContextManager extends InitializingCacheable implements StrategyContextManager {

    private static final ConcurrentMap<Class, StrategyBuilder> CACHE = new ConcurrentHashMap<>();

    static {
        CACHE.put(CacheStrategyBuilder.class, new StandardCacheStrategyBuilder());
        CACHE.put(ProxyStrategyBuilder.class, new StandardProxyStrategyBuilder());
        CACHE.put(LimitStrategyBuilder.class, new StandardLimitStrategyBuilder());
        CACHE.put(RetryStrategyBuilder.class, new StandardRetryStrategyBuilder());
        CACHE.put(RetryStrategyBuilder.class, new StandardRetryIfResultStrategyBuilder());
    }

    @Override
    public CacheStrategyBuilder createCacheStrategy() {
        return (CacheStrategyBuilder) CACHE.get(CacheStrategyBuilder.class);
    }

    @Override
    public ProxyStrategyBuilder createProxyStrategy() {
        return (ProxyStrategyBuilder) CACHE.get(ProxyStrategyBuilder.class);
    }

    @Override
    public LimitStrategyBuilder createLimitStrategy() {
        return (LimitStrategyBuilder) CACHE.get(LimitStrategyBuilder.class);
    }

    @Override
    public RetryStrategyBuilder createRetryIfResultStrategyBuilder() {
        return (RetryStrategyBuilder) CACHE.get(RetryStrategyBuilder.class);
    }

    @Override
    public RetryStrategyBuilder createRetryStrategyBuilder() {
        return (RetryStrategyBuilder) CACHE.get(RetryStrategyBuilder.class);
    }

    @Override
    public <Strategy> Strategy createStrategy(Class<Strategy> strategyClass) {
        return null == strategyClass ? null : (Strategy) CACHE.get(strategyClass);
    }

    @Override
    public StrategyBuilder strategy(Class aClass, StrategyBuilder strategyBuilder) {
        return CACHE.putIfAbsent(aClass, strategyBuilder);
    }
}

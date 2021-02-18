package com.chua.utils.tools.manager;

import com.chua.utils.tools.manager.builder.*;

/**
 * 策略管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public interface StrategyContextManager {
    /**
     * 缓存策略
     *
     * @return 缓存策略
     */
    CacheStrategyBuilder createCacheStrategy();

    /**
     * 代理策略
     *
     * @return 代理策略
     */
    ProxyStrategyBuilder createProxyStrategy();

    /**
     * 限流策略
     *
     * @return 限流策略
     */
    LimitStrategyBuilder createLimitStrategy();

    /**
     * 重试策略
     *
     * @return 重试策略
     */
    RetryStrategyBuilder createRetryIfResultStrategyBuilder();

    /**
     * 重试策略
     *
     * @return 重试策略
     */
    RetryStrategyBuilder createRetryStrategyBuilder();

    /**
     * 获取策略
     *
     * @param strategyClass 策略类型
     * @param <S>           策略类型
     * @return 策略
     */
    <S> S createStrategy(Class<S> strategyClass);

    /**
     * 设置构造器
     * <p>aClass为其它接口返回值</p>
     * <p>例如: {@link #createCacheStrategy()}的返回值{@link CacheStrategyBuilder}</p>
     *
     * @param aClass          类(为其它接口返回值)
     * @param strategyBuilder 构造器
     * @return StrategyBuilder
     */
    StrategyBuilder strategy(Class aClass, StrategyBuilder strategyBuilder);
}

package com.chua.utils.tools.strategy.enums;

/**
 * 策略
 * @author CH
 */
public enum StrategyType {
    /**
     * 重试
     */
    RETRY,
    /**
     * 重试
     */
    RETRY_CONDITION,
    /**
     * 代理
     */
    PROXY,
    /**
     * 缓存
     */
    CACHE,
    /**
     * 限流
     */
    LIMIT,
    /**
     * 超时
     */
    TIMEOUT;
}

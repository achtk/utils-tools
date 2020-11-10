package com.chua.utils.tools.manager.builder;

import java.util.function.Predicate;

/**
 * 限流策略
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public interface RetryStrategyBuilder<T> {

    /**
     * 重试
     *
     * @param retry     尝试次数
     * @param timeout   超时时间
     * @param predicate 重试条件
     * @return RetryCreateStrategyBuilder
     */
    RetryStrategyBuilder.RetryCreateStrategyBuilder<T> retry(final int retry, final long timeout, final Predicate predicate);

    /**
     * 重试
     *
     * @param retry     尝试次数
     * @param predicate 重试条件
     * @return RetryCreateStrategyBuilder
     */
    default RetryStrategyBuilder.RetryCreateStrategyBuilder<T> retry(final int retry, final Predicate predicate) {
        return retry(retry, 30000, predicate);
    }

    /**
     * 重试
     *
     * @param predicate 重试条件
     * @return RetryCreateStrategyBuilder
     */
    default RetryStrategyBuilder.RetryCreateStrategyBuilder<T> retry(final Predicate predicate) {
        return retry(3, 30000, predicate);
    }

    /**
     * 代理创建工厂
     */
    interface RetryCreateStrategyBuilder<T> extends StrategyBuilder<T> {
        /**
         * 创建缓存对象
         *
         * @param entity 对象
         * @return 对象
         */
        @Override
        T create(T entity);
    }
}

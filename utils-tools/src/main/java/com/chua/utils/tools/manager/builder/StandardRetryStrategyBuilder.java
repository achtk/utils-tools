package com.chua.utils.tools.manager.builder;

import com.chua.utils.tools.strategy.RetryStrategy;

import java.util.function.Predicate;

/**
 * 限流策略构造器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public class StandardRetryStrategyBuilder<T> implements RetryStrategyBuilder<T>, StrategyBuilder<T> {

    private int retry;
    private long timeout;
    private Predicate predicate;

    @Override
    public T create(T entity) {
        RetryStrategy<T> retryStrategy = new RetryStrategy<>();
        retryStrategy.setRetry(retry);
        retryStrategy.setTimeout(timeout);
        retryStrategy.setThrowPredicate(input -> predicate.test(input));
        return retryStrategy.create(entity);
    }

    @Override
    public RetryCreateStrategyBuilder<T> retry(int retry, long timeout, Predicate predicate) {
        this.retry = retry;
        this.timeout = timeout;
        this.predicate = predicate;

        return new RetryCreateStrategyBuilder<T>() {
            @Override
            public T create(T entity) {
                return StandardRetryStrategyBuilder.this.create(entity);
            }
        };
    }

}

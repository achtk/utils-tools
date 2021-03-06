package com.chua.utils.tools.manager.builder;

import com.chua.utils.tools.strategy.TokenLimitStrategy;

/**
 * 限流策略构造器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public class StandardLimitStrategyBuilder<T> implements LimitStrategyBuilder<T>, StrategyBuilder<T> {

    private double token;

    @Override
    public LimitCreateStrategyBuilder<T> limit(double token) {
        this.token = token;
        return entity -> StandardLimitStrategyBuilder.this.create(entity);
    }

    @Override
    public T create(T entity) {
        return new TokenLimitStrategy<T>(token).create(entity);
    }
}

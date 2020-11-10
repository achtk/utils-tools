package com.chua.utils.tools.manager.builder;

import com.chua.utils.tools.function.intercept.MethodIntercept;

/**
 * 限流策略
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public interface LimitStrategyBuilder<T> {

    /**
     * 令牌数量
     *
     * @param token 令牌数量
     * @return LimitStrategyBuilder
     */
    LimitStrategyBuilder.LimitCreateStrategyBuilder<T> limit(final double token);

    /**
     * 代理创建工厂
     */
    interface LimitCreateStrategyBuilder<T> extends StrategyBuilder<T> {
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

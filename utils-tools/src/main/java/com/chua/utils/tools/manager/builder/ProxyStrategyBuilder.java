package com.chua.utils.tools.manager.builder;

import com.chua.utils.tools.function.intercept.MethodIntercept;

/**
 * 代理策略构造器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public interface ProxyStrategyBuilder<T> {
    /**
     * 代理方法
     *
     * @param methodIntercept 代理方法
     * @return ProxyStrategyBuilder
     */
    ProxyCreateStrategyBuilder<T> proxy(MethodIntercept methodIntercept);

    /**
     * 代理创建工厂
     */
    interface ProxyCreateStrategyBuilder<T> extends StrategyBuilder<T> {
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

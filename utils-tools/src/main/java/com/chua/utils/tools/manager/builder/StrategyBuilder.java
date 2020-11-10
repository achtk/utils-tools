package com.chua.utils.tools.manager.builder;

/**
 * 策略构造
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public interface StrategyBuilder<T> {

    /**
     * 创建缓存对象
     *
     * @param entity 对象
     * @return 对象
     */
    T create(T entity);
}

package com.chua.utils.tools.strategy;

/**
 * 策略
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public interface Strategy<T> {
    /**
     * 创建策略
     *
     * @param source 对象
     * @return T
     */
    T create(T source);
}

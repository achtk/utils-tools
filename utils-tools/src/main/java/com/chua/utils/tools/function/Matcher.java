package com.chua.utils.tools.function;

/**
 * 匹配器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public interface Matcher<T> {

    /**
     * 回调
     *
     * @param item 元素
     * @throws Throwable 异常
     */
    void doWith(T item) throws Throwable;
}

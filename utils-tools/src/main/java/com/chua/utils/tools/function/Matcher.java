package com.chua.utils.tools.function;

import java.lang.reflect.Member;

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
     * @throws Throwable
     */
    void doWith(T item) throws Throwable;
}

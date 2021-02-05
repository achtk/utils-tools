package com.chua.utils.tools.function;

/**
 * 匹配器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
@FunctionalInterface
public interface BiMatcher<T, T2> {

    /**
     * 回调
     *
     * @param item  元素
     * @param item2 元素
     * @throws Exception Exception
     */
    void doWith(T item, T2 item2) throws Exception;
}

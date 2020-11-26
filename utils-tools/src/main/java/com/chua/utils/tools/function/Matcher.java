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
     */
    void doWith(T item);


    /**
     * Matcher不为空执行
     *
     * @param matcher Mathcer
     * @param data    数据
     * @see Matcher
     */
    static <T> void doWith(Matcher<T> matcher, T data) {
        if (null == matcher) {
            return;
        }
        matcher.doWith(data);
    }
}

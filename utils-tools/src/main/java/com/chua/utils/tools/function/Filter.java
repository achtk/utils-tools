package com.chua.utils.tools.function;

/**
 * 过滤器
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public interface Filter<T> {
    /**
     * 过滤器
     * @param item 元素
     * @return
     */
    boolean matcher(T item);
}

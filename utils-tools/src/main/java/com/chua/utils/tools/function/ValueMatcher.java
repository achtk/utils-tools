package com.chua.utils.tools.function;

/**
 * 数据匹配器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
@FunctionalInterface
public interface ValueMatcher<E> {
    /**
     * 是否匹配元素
     *
     * @param e 元素
     * @return 匹配返回true
     */
    boolean isMatcher(E e);
}

package com.chua.utils.tools.function;

/**
 * 回调
 * @author CH
 */
public interface IPredicate<I> {
    /**
     * 处理下一个
     * @param item 元素
     * @return
     */
    boolean test(I item);
}

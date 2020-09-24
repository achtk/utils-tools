package com.chua.unified.function;

/**
 * 方法
 * @author CH
 */
public interface IFunction<I, O> {

    /**
     * 消费
     * @param item
     * @return
     */
    O accept(I item);
}

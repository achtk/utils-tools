package com.chua.utils.tools.function;

/**
 * 消费者
 * @author CH
 */
public interface IConsumer<I> {
    /**
     * 处理下一个
     * @param item
     * @return
     */
    void next(I item);
}

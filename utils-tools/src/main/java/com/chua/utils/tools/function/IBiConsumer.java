package com.chua.utils.tools.function;

/**
 * 消费者
 * @author CH
 */
public interface IBiConsumer<I, U> {
    /**
     * 处理下一个
     * @param item 元素
     * @param uItem 元素
     * @return
     */
    void next(I item, U uItem);
}

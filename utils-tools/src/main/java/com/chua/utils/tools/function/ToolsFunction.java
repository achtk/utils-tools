package com.chua.utils.tools.function;

/**
 * 方法
 * @author CH
 */
@FunctionalInterface
public interface ToolsFunction<I, O> {

    /**
     * 消费
     * @param item
     * @return
     */
    O accept(I item);
}

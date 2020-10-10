package com.chua.utils.tools.function;

/**
 * 消费者
 * @author CH
 */
public interface ISupplier<O> {

    /**
     * 获取节点
     * @return 输出
     */
    O get();
}
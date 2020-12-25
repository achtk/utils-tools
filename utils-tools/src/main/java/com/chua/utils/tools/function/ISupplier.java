package com.chua.utils.tools.function;

/**
 * 提供者
 * @author CH
 */
@FunctionalInterface
public interface ISupplier<O> {

    /**
     * 获取节点
     * @return 输出
     */
    O get();
}

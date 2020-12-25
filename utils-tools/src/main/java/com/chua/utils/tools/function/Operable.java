package com.chua.utils.tools.function;

/**
 * 可操作接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
@FunctionalInterface
public interface Operable<I, O> {
    /**
     * 操作输出
     * @param source  输入
     * @return O
     */
    O operate(I source);
}

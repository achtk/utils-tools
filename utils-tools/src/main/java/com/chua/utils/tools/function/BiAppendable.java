package com.chua.utils.tools.function;

/**
 * 可追加
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
@FunctionalInterface
public interface BiAppendable<S, S1> {
    /**
     * 追加数据
     *
     * @param v1 值1
     * @param v2 值2
     * @return BiAppendable
     */
    BiAppendable append(S v1, S1 v2);
}

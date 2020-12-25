package com.chua.utils.tools.function;

/**
 * 转化器
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/24 21:37
 */
@FunctionalInterface
public interface Converter<I, O> {
    /**
     * 转化
     *
     * @param value  值
     * @param tClass 类型
     * @return O
     */
    O convert(I value, Class<? extends O> tClass);
}

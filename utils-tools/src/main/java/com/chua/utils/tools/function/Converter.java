package com.chua.utils.tools.function;

/**
 * 转化器
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/24 21:37
 */
public interface Converter<I, O> {
    /**
     * 转化
     *
     * @param value 值
     * @param type
     * @return o
     */
    O convert(I value, Class<?> type);
}

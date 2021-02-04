package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * 类型转化定义
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
@FunctionalInterface
public interface TypeConversionDefinition<S, T> {
    /**
     * 转化
     *
     * @param source 数据源
     * @return 转化类型
     */
    @Nullable
    T convert(S source);
}

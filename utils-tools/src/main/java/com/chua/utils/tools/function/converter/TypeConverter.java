package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.function.Converter;

/**
 * 类型转String
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public interface TypeConverter<O> extends Converter<Object, O> {

    /**
     * 类型转化
     *
     * @param value 值
     * @return O
     */
    O convert(Object value);

    /**
     * 转化类型
     *
     * @return O
     */
    Class<O> getType();

    /**
     * 转化
     *
     * @param value  值
     * @param tClass 类型
     * @return 值
     */
    @Override
    default O convert(Object value, Class<? extends O> tClass) {
        return convert(value);
    }
}

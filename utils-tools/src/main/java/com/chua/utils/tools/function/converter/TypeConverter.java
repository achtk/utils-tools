package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.function.Converter;

/**
 * 类型转String
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public abstract class TypeConverter<O> implements Converter<Object, O> {
    /**
     * 类型转化
     *
     * @param value 值
     * @return O
     */
    abstract public O convert(Object value);

    /**
     * 转化类型
     * @return O
     */
    abstract public Class<? extends O> getType();

    @Override
    public O convert(Object value, Class<? extends O> tClass) {
        return convert(value);
    }
}

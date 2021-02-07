package com.chua.utils.tools.function.converter;

/**
 * long类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class LongTypeConverter implements TypeConverter<Long> {

    @Override
    public Class<Long> getType() {
        return Long.class;
    }
}

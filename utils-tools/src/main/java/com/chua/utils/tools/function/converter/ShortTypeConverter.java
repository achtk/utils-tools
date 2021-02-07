package com.chua.utils.tools.function.converter;

/**
 * Float类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class ShortTypeConverter implements TypeConverter<Short> {

    @Override
    public Class<Short> getType() {
        return Short.class;
    }
}

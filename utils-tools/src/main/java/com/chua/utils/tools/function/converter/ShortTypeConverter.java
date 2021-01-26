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
    public Short convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }

        if (value instanceof String) {
            try {
                return Short.valueOf(value.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public Class<Short> getType() {
        return Short.class;
    }
}

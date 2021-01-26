package com.chua.utils.tools.function.converter;

/**
 * Float类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class FloatTypeConverter implements TypeConverter<Float> {
    @Override
    public Float convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }

        if (value instanceof String) {
            try {
                return Float.valueOf(value.toString());
            } catch (NumberFormatException ignore) {
            }
        }

        return null;
    }

    @Override
    public Class<Float> getType() {
        return Float.class;
    }
}

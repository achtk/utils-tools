package com.chua.utils.tools.function.converter;

/**
 * Integer类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class IntegerTypeConverter implements TypeConverter<Integer> {
    @Override
    public Integer convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        if (value instanceof String) {
            try {
                return Integer.valueOf(value.toString());
            } catch (NumberFormatException ignore) {
            }
        }

        return null;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}

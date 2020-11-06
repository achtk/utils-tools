package com.chua.utils.tools.function.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

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

        if (value instanceof BigInteger) {
            return ((BigInteger) value).floatValue();
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).floatValue();
        }

        if (value instanceof String) {
            try {
                return Float.valueOf(value.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public Class<? extends Float> getType() {
        return Float.class;
    }
}

package com.chua.utils.tools.function.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * long类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class LongTypeConverter implements TypeConverter<Long> {
    @Override
    public Long convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        if (value instanceof BigInteger) {
            return ((BigInteger) value).longValue();
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        }

        if (value instanceof String) {
            try {
                return Long.valueOf(value.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public Class<? extends Long> getType() {
        return Long.class;
    }
}

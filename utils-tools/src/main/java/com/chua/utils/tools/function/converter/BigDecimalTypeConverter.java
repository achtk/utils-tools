package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.classes.ClassHelper;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * BigDecimal转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class BigDecimalTypeConverter implements TypeConverter<BigDecimal> {
    @Override
    public BigDecimal convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }

        if (value instanceof Long) {
            return new BigDecimal((Long) value);
        }

        if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (Exception e) {
            }
        }

        if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        }

        if (value instanceof Double) {
            return new BigDecimal((Double) value);
        }

        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }

        if (value instanceof char[]) {
            return new BigDecimal((char[]) value);
        }
        return null;
    }

    @Override
    public Class<BigDecimal> getType() {
        return BigDecimal.class;
    }
}

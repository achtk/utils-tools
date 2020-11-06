package com.chua.utils.tools.function.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Double类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class ByteTypeConverter implements TypeConverter<Byte> {
    @Override
    public Byte convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }

        if (value instanceof BigInteger) {
            return ((BigInteger) value).byteValue();
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).byteValue();
        }

        if (value instanceof String) {
            try {
                return Byte.valueOf(value.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public Class<Byte> getType() {
        return Byte.class;
    }
}

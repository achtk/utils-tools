package com.chua.utils.tools.function.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * BigDecimal转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class BigIntegerTypeConverter implements TypeConverter<BigInteger> {
    @Override
    public BigInteger convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }

        if (value instanceof Long) {
            return BigInteger.valueOf((Long) value);
        }

        if (value instanceof String) {
            try {
                return new BigInteger((String) value);
            } catch (Exception e) {
            }
        }

        if (value instanceof byte[]) {
            return new BigInteger((byte[]) value);
        }
        return null;
    }

    @Override
    public Class<BigInteger> getType() {
        return BigInteger.class;
    }
}

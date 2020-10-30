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
public class ShortTypeConverter extends TypeConverter<Short> {
    @Override
    public Short convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }

        if (value instanceof BigInteger) {
            return ((BigInteger) value).shortValue();
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).shortValue();
        }


        if (value instanceof String) {
            return Short.valueOf(value.toString());
        }

        return null;
    }

    @Override
    public Class<? extends Short> getType() {
        return Short.class;
    }
}

package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.util.NumberUtils;

import java.math.BigDecimal;

/**
 * Number 转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class NumberTypeConverter implements TypeConverter<Number> {
    @Override
    public Number convert(Object value) {
        if (null == value) {
            return null;
        }

        if (Number.class.isAssignableFrom(value.getClass())) {
            return (Number) value;
        }
        return NumberUtils.parseNumber(value.toString(), BigDecimal.class);
    }

    @Override
    public Class<Number> getType() {
        return Number.class;
    }
}

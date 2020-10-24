package com.chua.utils.tools.classes.convertor;

import java.math.BigDecimal;

/**
 * 字符串转换器
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/24 21:07
 */
public class NumberConvertor implements Convertor<Number> {
    @Override
    public Long convert(Object value) {
        if(null == value) {
            return null;
        }

        if(value instanceof String) {
            return Long.parseLong(value.toString());
        }
        BigDecimal bigDecimal = getNumber(value);
        return null == bigDecimal ? null : bigDecimal.longValue();
    }
}

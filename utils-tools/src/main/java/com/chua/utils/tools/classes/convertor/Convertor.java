package com.chua.utils.tools.classes.convertor;

import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 转换器
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/24 21:06
 */
public interface Convertor<T> {
    /**
     * 转换
     *
     * @param value 数据源
     * @return
     */
    T convert(Object value);

    /**
     * 分隔符
     *
     * @return
     */
    default String delimiter() {
        return ",";
    }

    /**
     * 获取BigDecimal
     *
     * @param value
     * @return
     */
    default BigDecimal getNumber(Object value) {
        if (value instanceof Double) {
            return new BigDecimal((Double) value);
        }
        if (value instanceof Float) {
            return new BigDecimal((Float) value);
        }
        if (value instanceof Character) {
            return new BigDecimal((Character) value);
        }
        if (value instanceof Short) {
            return new BigDecimal((Short) value);
        }
        if (value instanceof Long) {
            return new BigDecimal((Long) value);
        }
        if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        }
        if (value instanceof char[]) {
            return new BigDecimal((char[]) value);
        }
        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        return null;
    }
}

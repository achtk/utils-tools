package com.chua.utils.tools.function.converter;

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
    public Class<BigInteger> getType() {
        return BigInteger.class;
    }
}

package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.NumberUtils;

import javax.annotation.Nullable;
import java.math.BigInteger;

/**
 * number -> BigInteger
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class NumberToBigIntegerConversionDefinition implements TypeConversionDefinition<Number, BigInteger> {
    @Nullable
    @Override
    public BigInteger convert(Number source) {
        return null == source ? null : NumberUtils.parseNumber(source, BigInteger.class);
    }
}

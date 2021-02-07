package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.math.BigInteger;

/**
 * string -> BigInteger
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToBigIntegerConversionDefinition implements TypeConversionDefinition<String, BigInteger> {
    @Nullable
    @Override
    public BigInteger convert(String source) {
        try {
            return new BigInteger(source);
        } catch (Exception ignore) {
            return BigInteger.ZERO;
        }
    }
}

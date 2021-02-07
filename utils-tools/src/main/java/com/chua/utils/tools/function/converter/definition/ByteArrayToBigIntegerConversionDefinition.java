package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.math.BigInteger;

/**
 * byte[] -> BigInteger
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class ByteArrayToBigIntegerConversionDefinition implements TypeConversionDefinition<byte[], BigInteger> {
    @Nullable
    @Override
    public BigInteger convert(byte[] source) {
        return null == source ? null : new BigInteger(source);
    }
}

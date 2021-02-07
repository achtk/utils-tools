package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.math.BigDecimal;

/**
 * char[] -> BigDecimal
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class CharArrayToBigDecimalConversionDefinition implements TypeConversionDefinition<char[], BigDecimal> {
    @Nullable
    @Override
    public BigDecimal convert(char[] source) {
        return null == source ? null : new BigDecimal(source);
    }
}

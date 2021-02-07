package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.math.BigDecimal;

/**
 * string -> BigDecimal
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToBigDecimalConversionDefinition implements TypeConversionDefinition<String, BigDecimal> {
    @Nullable
    @Override
    public BigDecimal convert(String source) {
        try {
            return new BigDecimal(source);
        } catch (Exception ignore) {
            return BigDecimal.ZERO;
        }
    }
}

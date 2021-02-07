package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.NumberUtils;

import javax.annotation.Nullable;
import java.math.BigDecimal;

/**
 * number -> BigDecimal
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class NumberToBigDecimalConversionDefinition implements TypeConversionDefinition<Number, BigDecimal> {
    @Nullable
    @Override
    public BigDecimal convert(Number source) {
        return null == source ? null : NumberUtils.parseNumber(source, BigDecimal.class);
    }
}

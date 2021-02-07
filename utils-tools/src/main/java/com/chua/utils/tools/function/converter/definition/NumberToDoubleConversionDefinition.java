package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * number -> double
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class NumberToDoubleConversionDefinition implements TypeConversionDefinition<Number, Double> {
    @Nullable
    @Override
    public Double convert(Number source) {
        return null == source ? null : source.doubleValue();
    }
}

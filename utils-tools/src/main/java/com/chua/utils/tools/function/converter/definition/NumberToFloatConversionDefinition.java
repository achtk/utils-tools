package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * number -> float
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class NumberToFloatConversionDefinition implements TypeConversionDefinition<Number, Float> {
    @Nullable
    @Override
    public Float convert(Number source) {
        return null == source ? null : source.floatValue();
    }
}

package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * number -> Integer
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class NumberToIntegerConversionDefinition implements TypeConversionDefinition<Number, Integer> {
    @Nullable
    @Override
    public Integer convert(Number source) {
        return null == source ? null : source.intValue();
    }
}

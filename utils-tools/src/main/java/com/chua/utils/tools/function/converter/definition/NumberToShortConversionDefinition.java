package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * number -> Short
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class NumberToShortConversionDefinition implements TypeConversionDefinition<Number, Short> {
    @Nullable
    @Override
    public Short convert(Number source) {
        return null == source ? null : source.shortValue();
    }
}

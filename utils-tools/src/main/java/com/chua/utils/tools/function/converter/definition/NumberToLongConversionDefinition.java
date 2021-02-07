package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * number -> Long
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class NumberToLongConversionDefinition implements TypeConversionDefinition<Number, Long> {
    @Nullable
    @Override
    public Long convert(Number source) {
        return null == source ? null : source.longValue();
    }
}

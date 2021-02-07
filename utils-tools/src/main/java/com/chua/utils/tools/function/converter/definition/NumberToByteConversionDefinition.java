package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * number -> byte
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class NumberToByteConversionDefinition implements TypeConversionDefinition<Number, Byte> {
    @Nullable
    @Override
    public Byte convert(Number source) {
        return null == source ? null : source.byteValue();
    }
}

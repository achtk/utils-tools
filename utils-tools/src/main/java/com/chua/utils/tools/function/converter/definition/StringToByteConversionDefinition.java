package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * string -> Byte
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToByteConversionDefinition implements TypeConversionDefinition<String, Byte> {
    @Nullable
    @Override
    public Byte convert(String source) {
        try {
            return null == source ? null : Byte.valueOf(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

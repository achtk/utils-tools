package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * string -> Float
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class String1ToFloatConversionDefinition implements TypeConversionDefinition<String, Float> {
    @Nullable
    @Override
    public Float convert(String source) {
        try {
            return null == source ? null : Float.valueOf(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

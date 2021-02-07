package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * string -> Integer
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToIntegerConversionDefinition implements TypeConversionDefinition<String, Integer> {
    @Nullable
    @Override
    public Integer convert(String source) {
        try {
            return null == source ? null : Integer.valueOf(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * string -> Short
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToShortConversionDefinition implements TypeConversionDefinition<String, Short> {
    @Nullable
    @Override
    public Short convert(String source) {
        try {
            return null == source ? null : Short.valueOf(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

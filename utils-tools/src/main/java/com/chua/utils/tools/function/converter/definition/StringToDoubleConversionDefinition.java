package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * string -> Double
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToDoubleConversionDefinition implements TypeConversionDefinition<String, Double> {
    @Nullable
    @Override
    public Double convert(String source) {
        try {
            return null == source ? null : Double.valueOf(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

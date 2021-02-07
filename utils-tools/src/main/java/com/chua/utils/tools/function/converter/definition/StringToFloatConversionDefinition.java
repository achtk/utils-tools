package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.aware.OrderAware;

import javax.annotation.Nullable;

/**
 * string -> Float
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToFloatConversionDefinition implements TypeConversionDefinition<String, Float>, OrderAware {
    @Nullable
    @Override
    public Float convert(String source) {
        try {
            return null == source ? null : Float.valueOf(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public int order() {
        return 10;
    }
}

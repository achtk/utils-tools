package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;

/**
 * Object -> Void
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
public class VoidConversionDefinition implements TypeConversionDefinition<Object, Void> {

    @Nullable
    @Override
    public Void convert(Object source) {
        return null;
    }
}

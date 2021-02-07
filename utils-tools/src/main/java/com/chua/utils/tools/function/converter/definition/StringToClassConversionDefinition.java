package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.classes.ClassHelper;

import javax.annotation.Nullable;

/**
 * string -> class
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToClassConversionDefinition implements TypeConversionDefinition<String, Class> {
    @Nullable
    @Override
    public Class convert(String source) {
        return ClassHelper.forName(source);
    }
}

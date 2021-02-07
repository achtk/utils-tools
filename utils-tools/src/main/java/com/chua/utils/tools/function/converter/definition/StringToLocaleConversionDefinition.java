package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.StringUtils;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * String -> Locale
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToLocaleConversionDefinition implements TypeConversionDefinition<String, Locale>{
    @Nullable
    @Override
    public Locale convert(String source) {
        try {
            return StringUtils.parseLocaleString(source);
        } catch (Exception e) {
            return Locale.getDefault();
        }
    }
}

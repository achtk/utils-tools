package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * LocaleCategory -> Locale
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class LocaleCategoryToLocaleConversionDefinition implements TypeConversionDefinition<Locale.Category, Locale> {
    @Nullable
    @Override
    public Locale convert(Locale.Category source) {
        return Locale.getDefault(source);
    }
}

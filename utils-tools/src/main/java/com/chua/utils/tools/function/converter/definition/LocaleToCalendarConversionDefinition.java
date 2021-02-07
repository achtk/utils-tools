package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Locale;

/**
 *
 * Locale -> Calendar
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class LocaleToCalendarConversionDefinition implements TypeConversionDefinition<Locale, Calendar> {
    @Nullable
    @Override
    public Calendar convert(Locale source) {
        return Calendar.getInstance(source);
    }
}

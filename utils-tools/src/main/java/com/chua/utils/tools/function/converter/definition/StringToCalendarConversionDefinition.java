package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * String -> Calendar
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToCalendarConversionDefinition implements TypeConversionDefinition<String, Calendar> {
    @Nullable
    @Override
    public Calendar convert(String source) {
        if (Strings.isNullOrEmpty(source)) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(DateUtils.parseDate(source));
        } catch (ParseException e) {
            return null;
        }
        return calendar;
    }
}

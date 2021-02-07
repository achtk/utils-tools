package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * LocalDateTime -> Calendar
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class LocalDateTimeToCalendarConversionDefinition implements TypeConversionDefinition<LocalDateTime, Calendar> {
    @Nullable
    @Override
    public Calendar convert(LocalDateTime source) {
        if (null == source) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(DateUtils.parseDate(source));
        return calendar;
    }
}

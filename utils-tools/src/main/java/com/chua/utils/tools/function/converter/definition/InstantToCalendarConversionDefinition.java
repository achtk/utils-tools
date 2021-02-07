package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Instant -> Calendar
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class InstantToCalendarConversionDefinition implements TypeConversionDefinition<Instant, Calendar> {
    @Nullable
    @Override
    public Calendar convert(Instant source) {
        if (null == source) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(DateUtils.parseDate(source));
        return calendar;
    }
}

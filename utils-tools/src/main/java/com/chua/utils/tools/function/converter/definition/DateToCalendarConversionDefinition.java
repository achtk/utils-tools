package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Date -> Calendar
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class DateToCalendarConversionDefinition implements TypeConversionDefinition<Date, Calendar> {
    @Nullable
    @Override
    public Calendar convert(Date source) {
        if (null == source) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(source);
        return calendar;
    }
}

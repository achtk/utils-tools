package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * LocalDate -> Calendar
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class LocalDateToCalendarConversionDefinition implements TypeConversionDefinition<LocalDate, Calendar> {
    @Nullable
    @Override
    public Calendar convert(LocalDate source) {
        if (null == source) {
            return null;
        }
        LocalDate localDate = (LocalDate) source;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(DateUtils.parseDate(localDate));
        return calendar;
    }
}

package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * TimeZone -> Calendar
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class TimeZoneToCalendarConversionDefinition implements TypeConversionDefinition<TimeZone, Calendar> {
    @Nullable
    @Override
    public Calendar convert(TimeZone source) {
        return Calendar.getInstance(source);
    }
}

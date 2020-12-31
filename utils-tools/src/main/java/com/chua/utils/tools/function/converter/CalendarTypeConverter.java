package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.util.ClassUtils;
import com.chua.utils.tools.util.DateUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * boolean类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class CalendarTypeConverter implements TypeConverter<Calendar> {

    @Override
    public Calendar convert(Object source) {
        if (null == source) {
            return null;
        }

        if (ClassUtils.isAssignableFrom(source.getClass(), Calendar.class)) {
            return (Calendar) source;
        }

        if (ClassUtils.isAssignableFrom(source.getClass(), TimeZone.class)) {
            return Calendar.getInstance((TimeZone) source);
        }

        if (ClassUtils.isAssignableFrom(source.getClass(), Locale.class)) {
            return Calendar.getInstance((Locale) source);
        }

        if (ClassUtils.isAssignableFrom(source.getClass(), Date.class)) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime((Date) source);
            return calendar;
        }

        if (ClassUtils.isAssignableFrom(source.getClass(), LocalDate.class)) {
            LocalDate localDate = (LocalDate) source;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(DateUtils.parseDate(localDate));
            return calendar;
        }

        if (ClassUtils.isAssignableFrom(source.getClass(), LocalDateTime.class)) {
            LocalDateTime localDateTime = (LocalDateTime) source;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(DateUtils.parseDate(localDateTime));
            return calendar;
        }

        if (ClassUtils.isAssignableFrom(source.getClass(), Instant.class)) {
            Instant instant = (Instant) source;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(DateUtils.parseDate(instant));
            return calendar;
        }

        if (source instanceof String) {
            Calendar calendar = new GregorianCalendar();
            try {
                calendar.setTime(DateUtils.parseDate(source.toString()));
            } catch (ParseException e) {
                return null;
            }
            return calendar;
        }
        return null;
    }

    @Override
    public Class<Calendar> getType() {
        return Calendar.class;
    }
}

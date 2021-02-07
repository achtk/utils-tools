package com.chua.utils.tools.function.converter;

import java.util.Calendar;

/**
 * boolean类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class CalendarTypeConverter implements TypeConverter<Calendar> {

    @Override
    public Class<Calendar> getType() {
        return Calendar.class;
    }
}

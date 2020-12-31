package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.util.StringUtils;

import java.util.TimeZone;

/**
 * TimeZone 转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class TimeZoneTypeConverter implements TypeConverter<TimeZone> {
    @Override
    public TimeZone convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof TimeZone) {
            return (TimeZone) value;
        }
        return StringUtils.parseTimeZoneString(value.toString());
    }

    @Override
    public Class<TimeZone> getType() {
        return TimeZone.class;
    }
}

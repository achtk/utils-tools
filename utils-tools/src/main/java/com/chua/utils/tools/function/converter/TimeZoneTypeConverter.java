package com.chua.utils.tools.function.converter;

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
    public Class<TimeZone> getType() {
        return TimeZone.class;
    }
}

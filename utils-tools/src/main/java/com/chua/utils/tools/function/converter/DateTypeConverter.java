package com.chua.utils.tools.function.converter;

import java.util.Date;

/**
 * 时间格式转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class DateTypeConverter implements TypeConverter<Date> {

    @Override
    public Class<Date> getType() {
        return Date.class;
    }
}

package com.chua.utils.tools.function.converter;

import java.time.LocalDate;

/**
 * localDate
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/26
 */
public class LocalDateTypeConverter implements TypeConverter<LocalDate> {

    @Override
    public Class<LocalDate> getType() {
        return LocalDate.class;
    }
}

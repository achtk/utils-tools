package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.util.DateUtils;
import com.chua.utils.tools.util.NumberUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * localDate
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/26
 */
public class LocalDateTypeConverter implements TypeConverter<LocalDate> {
    @Override
    public LocalDate convert(Object value) {
        if (null == value || value instanceof LocalDate) {
            return (LocalDate) value;
        }

        try {
            if (value instanceof Date) {
                return DateUtils.toLocalDate((Date) value);
            }

            if (value instanceof LocalDateTime) {
                return DateUtils.toLocalDate((LocalDateTime) value);
            }

            if (value instanceof Instant) {
                return DateUtils.toLocalDate((Instant) value);
            }

            if (value instanceof Long) {
                return DateUtils.toLocalDate((Long) value);
            }

            if (value instanceof TemporalAccessor) {
                return DateUtils.toLocalDate((TemporalAccessor) value);
            }

        } catch (Exception ignore) {
        }

        if (value instanceof String && NumberUtils.isNumber(value.toString())) {
            try {
                return DateUtils.toLocalDate(Long.parseLong(value.toString()));
            } catch (NumberFormatException ignore) {
            }
        }

        return null;
    }

    @Override
    public Class<LocalDate> getType() {
        return LocalDate.class;
    }
}

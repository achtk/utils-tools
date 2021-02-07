package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LocalDateTime -> date
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
public class LocalDateTimeToLocalDateConversionDefinition implements TypeConversionDefinition<LocalDateTime, LocalDate> {

    @Nullable
    @Override
    public LocalDate convert(LocalDateTime source) {
        return DateUtils.toLocalDate(source);
    }
}

package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Instant -> LocalDate
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
public class InstantToLocalDateConversionDefinition implements TypeConversionDefinition<Instant, LocalDate> {

    @Nullable
    @Override
    public LocalDate convert(Instant source) {
        return DateUtils.toLocalDate(source);
    }
}

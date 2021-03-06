package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

/**
 * TemporalAccessor -> LocalDate
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
public class TemporalAccessorToLocalDateConversionDefinition implements TypeConversionDefinition<TemporalAccessor, LocalDate> {

    @Nullable
    @Override
    public LocalDate convert(TemporalAccessor source) {
        return DateUtils.toLocalDate(source);
    }
}

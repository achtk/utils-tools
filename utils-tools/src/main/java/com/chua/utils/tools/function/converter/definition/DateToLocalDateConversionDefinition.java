package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.Date;

/**
 * Date -> LocalDate
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class DateToLocalDateConversionDefinition implements TypeConversionDefinition<Date, LocalDate> {
    @Nullable
    @Override
    public LocalDate convert(Date source) {
        return null == source ? null : DateUtils.toLocalDate(source);
    }
}

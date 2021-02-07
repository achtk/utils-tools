package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.time.LocalDate;

/**
 * Long -> LocalDate
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
public class LongToLocalDateConversionDefinition implements TypeConversionDefinition<Long, LocalDate> {

    @Nullable
    @Override
    public LocalDate convert(Long source) {
        return DateUtils.toLocalDate(source);
    }
}

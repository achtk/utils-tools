package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Date;

/**
 * Instant -> date
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
public class InstantToDateConversionDefinition implements TypeConversionDefinition<Instant, Date> {

    @Nullable
    @Override
    public Date convert(Instant source) {
        return DateUtils.toDate(source);
    }
}

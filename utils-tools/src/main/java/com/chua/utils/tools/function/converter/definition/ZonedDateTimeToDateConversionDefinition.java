package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * LocalDate -> date
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
public class ZonedDateTimeToDateConversionDefinition implements TypeConversionDefinition<ZonedDateTime, Date> {

    @Nullable
    @Override
    public Date convert(ZonedDateTime source) {
        return DateUtils.parserDate(source);
    }
}

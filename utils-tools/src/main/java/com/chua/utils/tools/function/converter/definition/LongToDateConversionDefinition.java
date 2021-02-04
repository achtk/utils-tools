package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * long -> date
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
public class LongToDateConversionDefinition implements TypeConversionDefinition<Long, Date> {

    @Nullable
    @Override
    public Date convert(Long source) {
        return DateUtils.parseDate(source);
    }
}

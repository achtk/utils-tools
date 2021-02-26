package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * number -> date
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class NumberToDateConversionDefinition implements TypeConversionDefinition<Number, Date> {
    @Nullable
    @Override
    public Date convert(Number source) {
        return null == source ? null : DateUtils.parseDate(source.longValue());
    }
}

package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * Date -> Long
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class DateToLongConversionDefinition implements TypeConversionDefinition<Date, Long> {
    @Nullable
    @Override
    public Long convert(Date source) {
        return null == source ? null : source.getTime();
    }
}

package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.StringUtils;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.util.TimeZone;

/**
 * String -> TimeZone
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToTimeZoneConversionDefinition implements TypeConversionDefinition<String, TimeZone> {
    @Nullable
    @Override
    public TimeZone convert(String source) {
        if (Strings.isNullOrEmpty(source)) {
            return null;
        }
        return StringUtils.parseTimeZoneString(source);
    }
}

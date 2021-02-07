package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.DateUtils;
import com.chua.utils.tools.util.NumberUtils;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.time.LocalDate;

/**
 * String -> LocalDate
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
public class StringToLocalDateConversionDefinition implements TypeConversionDefinition<String, LocalDate> {

    @Nullable
    @Override
    public LocalDate convert(String source) {
        if (!Strings.isNullOrEmpty(source) && source instanceof String && NumberUtils.isNumber(source)) {
            try {
                return DateUtils.toLocalDate(Long.parseLong(source));
            } catch (NumberFormatException ignore) {
            }
        }
        return null;
    }
}

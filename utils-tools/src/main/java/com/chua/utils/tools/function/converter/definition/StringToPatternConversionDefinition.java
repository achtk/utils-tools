package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.constant.PatternConstant;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

/**
 * String -> Pattern
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToPatternConversionDefinition implements TypeConversionDefinition<String, Pattern> {
    @Nullable
    @Override
    public Pattern convert(String source) {
        if (Strings.isNullOrEmpty(source)) {
            return PatternConstant.REGEXP_ANY;
        }
        try {
            return Pattern.compile(source);
        } catch (Exception e) {
            return PatternConstant.REGEXP_ANY;
        }
    }
}

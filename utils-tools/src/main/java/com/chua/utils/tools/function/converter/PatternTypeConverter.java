package com.chua.utils.tools.function.converter;

import java.util.regex.Pattern;

/**
 * 正则
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public class PatternTypeConverter implements TypeConverter<Pattern> {

    @Override
    public Class<Pattern> getType() {
        return Pattern.class;
    }
}

package com.chua.utils.tools.function.converter.definition;

import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * String -> Charset
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToCharsetConversionDefinition implements TypeConversionDefinition<String, Charset> {
    @Nullable
    @Override
    public Charset convert(String source) {
        if (Strings.isNullOrEmpty(source)) {
            return UTF_8;
        }
        try {
            return Charset.forName(source);
        } catch (Exception e) {
            return UTF_8;
        }
    }
}

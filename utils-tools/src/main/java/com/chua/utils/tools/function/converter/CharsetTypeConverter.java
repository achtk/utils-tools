package com.chua.utils.tools.function.converter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Charset 转化
 * <br />默认返回: UTF-8
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class CharsetTypeConverter implements TypeConverter<Charset> {
    @Override
    public Charset convert(Object value) {
        if (null == value) {
            return null;
        }

        if (Charset.class.isAssignableFrom(value.getClass())) {
            return (Charset) value;
        }
        try {
            return Charset.forName(value.toString());
        } catch (Exception e) {
            return StandardCharsets.UTF_8;
        }
    }

    @Override
    public Class<Charset> getType() {
        return Charset.class;
    }
}

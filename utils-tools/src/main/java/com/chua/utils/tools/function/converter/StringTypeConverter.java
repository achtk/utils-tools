package com.chua.utils.tools.function.converter;

/**
 * 字符串转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class StringTypeConverter implements TypeConverter<String> {
    @Override
    public String convert(Object value) {
        return null == value ? null : value + "";
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}

package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.util.StringUtils;

import java.util.Locale;

/**
 * Locale 转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class LocaleTypeConverter implements TypeConverter<Locale> {
    @Override
    public Locale convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Locale) {
            return (Locale) value;
        }

        if (value instanceof Locale.Category) {
            return Locale.getDefault((Locale.Category) value);
        }
        try {
            return StringUtils.parseLocaleString(value.toString());
        } catch (Exception e) {
            return Locale.getDefault();
        }
    }

    @Override
    public Class<Locale> getType() {
        return Locale.class;
    }
}

package com.chua.utils.tools.function.converter;

import java.util.Currency;
import java.util.Locale;

/**
 * Currency转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class CurrencyTypeConverter implements TypeConverter<Currency> {
    @Override
    public Currency convert(Object value) {
        if (null == value) {
            return null;
        }

        if (Currency.class.isAssignableFrom(value.getClass())) {
            return (Currency) value;
        }

        if (Locale.class.isAssignableFrom(value.getClass())) {
            return Currency.getInstance((Locale) value);
        }
        try {
            return Currency.getInstance(value.toString());
        } catch (Exception ignore) {
        }
        return null;
    }

    @Override
    public Class<Currency> getType() {
        return Currency.class;
    }
}

package com.chua.utils.tools.function.converter;

import java.util.Currency;

/**
 * Currency转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class CurrencyTypeConverter implements TypeConverter<Currency> {

    @Override
    public Class<Currency> getType() {
        return Currency.class;
    }
}

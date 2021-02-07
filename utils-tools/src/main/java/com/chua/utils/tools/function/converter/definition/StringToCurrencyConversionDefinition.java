package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.util.Currency;

/**
 * String -> Currency
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class StringToCurrencyConversionDefinition implements TypeConversionDefinition<String, Currency> {
    @Nullable
    @Override
    public Currency convert(String source) {
        if(null == source) {
            return null;
        }

        try {
            return Currency.getInstance(source);
        } catch (Exception ignore) {
        }
        return null;
    }
}

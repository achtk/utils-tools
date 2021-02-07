package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.util.Currency;
import java.util.Locale;

/**
 * locale -> Currency
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class LocaleToCurrencyConversionDefinition implements TypeConversionDefinition<Locale, Currency> {
    @Nullable
    @Override
    public Currency convert(Locale source) {
        return null == source ? null : Currency.getInstance(source);
    }
}

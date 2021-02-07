package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.constant.SymbolConstant;

import javax.annotation.Nullable;

/**
 * object -> string
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class ObjectToStringConversionDefinition implements TypeConversionDefinition<Object, String> {
    @Nullable
    @Override
    public String convert(Object source) {
        return null == source ? SymbolConstant.SYMBOL_EMPTY : source.toString();
    }
}

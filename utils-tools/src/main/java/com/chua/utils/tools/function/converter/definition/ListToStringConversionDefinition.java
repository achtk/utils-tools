package com.chua.utils.tools.function.converter.definition;

import com.google.common.base.Joiner;

import javax.annotation.Nullable;
import java.util.List;

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_DOT;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_EMPTY;

/**
 * List -> string
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class ListToStringConversionDefinition implements TypeConversionDefinition<List<?>, String> {

    @Nullable
    @Override
    public String convert(List<?> source) {
        return null == source ? SYMBOL_EMPTY : Joiner.on(SYMBOL_DOT).join(source);
    }
}

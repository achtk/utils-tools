package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * List -> Iterable
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class ListToIterableConversionDefinition implements TypeConversionDefinition<List<?>, Iterable<?>> {

    @Nullable
    @Override
    public Iterable<?> convert(List<?> source) {
        return () -> (Iterator<Object>) Optional.ofNullable(source).orElse(Collections.emptyList()).iterator();
    }
}

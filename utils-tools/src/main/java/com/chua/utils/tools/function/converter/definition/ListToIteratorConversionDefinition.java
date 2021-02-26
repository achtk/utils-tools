package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * List -> Iterator
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class ListToIteratorConversionDefinition implements TypeConversionDefinition<List<?>, Iterator<?>> {

    @Nullable
    @Override
    public Iterator<?> convert(List<?> source) {
        return Optional.ofNullable(source).orElse(Collections.emptyList()).iterator();
    }
}

package com.chua.utils.tools.function.converter.definition;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * array -> list
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class ArrayToListConversionDefinition implements TypeConversionDefinition<Array, List> {

    @Nullable
    @Override
    public List convert(Array source) {
        return null == source ? Collections.emptyList() : Arrays.asList(source);
    }
}

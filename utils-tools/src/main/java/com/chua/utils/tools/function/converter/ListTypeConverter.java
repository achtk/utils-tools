package com.chua.utils.tools.function.converter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 字符串转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class ListTypeConverter implements TypeConverter<List> {
    @Override
    public List convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof List) {
            return (List) value;
        }

        if (value.getClass().isArray()) {
            return Arrays.asList(value);
        }
        return Collections.singletonList(value);
    }

    @Override
    public Class<List> getType() {
        return List.class;
    }
}

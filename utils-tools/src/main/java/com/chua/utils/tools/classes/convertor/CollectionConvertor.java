package com.chua.utils.tools.classes.convertor;

import com.google.common.base.Splitter;

import java.util.Arrays;
import java.util.Collection;

/**
 * 字符串转换器
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/24 21:07
 */
public class CollectionConvertor implements Convertor<Collection> {
    @Override
    public Collection convert(Object value) {
        if (null == value) {
            return null;
        }
        if (value instanceof Collection) {
            return (Collection) value;
        }
        if (value instanceof String) {
            return Splitter.on(delimiter()).omitEmptyStrings().trimResults().splitToList(value.toString());
        }

        if (value.getClass().isArray()) {
            return (Arrays.asList(value));
        }

        return null;
    }
}

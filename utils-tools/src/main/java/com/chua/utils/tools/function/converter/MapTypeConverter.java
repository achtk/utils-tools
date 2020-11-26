package com.chua.utils.tools.function.converter;

import java.util.Map;

/**
 * Map转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class MapTypeConverter implements TypeConverter<Map> {
    @Override
    public Map convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Map) {
            return (Map) value;
        }

        return null;
    }

    @Override
    public Class<Map> getType() {
        return Map.class;
    }
}

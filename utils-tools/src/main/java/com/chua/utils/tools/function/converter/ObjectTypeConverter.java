package com.chua.utils.tools.function.converter;

/**
 * null处理
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/27
 */
public class ObjectTypeConverter implements TypeConverter<Object> {
    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public Class<Object> getType() {
        return Object.class;
    }
}

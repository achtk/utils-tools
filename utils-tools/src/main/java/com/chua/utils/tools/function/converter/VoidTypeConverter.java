package com.chua.utils.tools.function.converter;

/**
 * null处理
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/27
 */
public class VoidTypeConverter implements TypeConverter<Void> {
    @Override
    public Void convert(Object value) {
        return null;
    }

    @Override
    public Class<Void> getType() {
        return Void.class;
    }
}

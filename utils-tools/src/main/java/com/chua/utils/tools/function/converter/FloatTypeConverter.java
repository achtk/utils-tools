package com.chua.utils.tools.function.converter;

/**
 * Float类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class FloatTypeConverter implements TypeConverter<Float> {

    @Override
    public Class<Float> getType() {
        return Float.class;
    }
}

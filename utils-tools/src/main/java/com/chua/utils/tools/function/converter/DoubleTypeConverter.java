package com.chua.utils.tools.function.converter;

/**
 * Double类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class DoubleTypeConverter implements TypeConverter<Double> {

    @Override
    public Class<Double> getType() {
        return Double.class;
    }
}

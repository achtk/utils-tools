package com.chua.utils.tools.function.converter;

/**
 * Integer类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class IntegerTypeConverter implements TypeConverter<Integer> {

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}

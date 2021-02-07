package com.chua.utils.tools.function.converter;

/**
 * boolean类型转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class BooleanTypeConverter implements TypeConverter<Boolean> {

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }
}

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
    public Boolean convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if(value instanceof String) {
            try {
                return Boolean.valueOf(value.toString());
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }
}

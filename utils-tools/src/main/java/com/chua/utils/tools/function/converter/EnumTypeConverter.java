package com.chua.utils.tools.function.converter;

/**
 * 正则
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public class EnumTypeConverter implements TypeConverter<Enum> {

    @Override
    public Enum convert(Object value, Class<? extends Enum> tClass) {
        if (null == value) {
            return null;
        }

        if (value instanceof Enum) {
            return (Enum) value;
        }

        if (null == tClass) {
            return null;
        }

        if (value instanceof String) {
            return Enum.valueOf(tClass, value.toString());
        }
        return null;
    }

    @Override
    public Class<Enum> getType() {
        return Enum.class;
    }
}

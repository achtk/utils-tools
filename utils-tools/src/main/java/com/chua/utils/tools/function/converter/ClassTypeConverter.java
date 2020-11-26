package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.classes.ClassHelper;

/**
 * 类转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class ClassTypeConverter implements TypeConverter<Class> {
    @Override
    public Class convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Class) {
            return (Class<?>) value;
        }

        if (value instanceof String) {
            return ClassHelper.forName(value.toString());
        }

        return null;
    }

    @Override
    public Class<Class> getType() {
        return Class.class;
    }
}

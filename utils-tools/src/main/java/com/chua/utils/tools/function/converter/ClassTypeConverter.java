package com.chua.utils.tools.function.converter;

/**
 * 类转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class ClassTypeConverter implements TypeConverter<Class> {

    @Override
    public Class<Class> getType() {
        return Class.class;
    }
}

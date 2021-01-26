package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.spi.factory.ExtensionFactory;

import java.util.Map;
import java.util.Optional;

/**
 * 转换器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public class Converter {

    private static final TypeConverter VOID_TYPE_CONVERTER = new VoidTypeConverter();


    private static final Map<Class, TypeConverter> TYPE_CONVERTER = ExtensionFactory.getExtensionLoader(TypeConverter.class).toPriorityMap(typeConverterExtensionClass -> typeConverterExtensionClass.getObj().getType());

    /**
     * 获取类型转换器
     *
     * @param type 类型
     * @return 类型转化器
     * @see TypeConverter
     * @since 1.0.0
     */
    public static TypeConverter getTypeConverter(Class<?> type) {
        return null == type ? VOID_TYPE_CONVERTER : Optional.ofNullable(TYPE_CONVERTER.get(type)).orElse(VOID_TYPE_CONVERTER);
    }


    /**
     * 数据转化
     *
     * @param value  数据
     * @param target 目标类型
     * @param <T>    类型
     * @return 目标类型
     */
    public static <T> T convertIfNecessary(Object value, Class<T> target) {
        TypeConverter typeConverter = getTypeConverter(target);
        return null == typeConverter ? null : (T) typeConverter.convert(value);
    }

    /**
     * 注入类型转化器
     *
     * @param type          类型
     * @param typeConverter 类型转化器
     */
    public static void addTypeConverter(Class<?> type, final TypeConverter typeConverter) {
        TYPE_CONVERTER.put(type, typeConverter);
    }
}

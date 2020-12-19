package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.spi.factory.ExtensionFactory;

import java.util.Map;

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
        return null == type ? VOID_TYPE_CONVERTER : TYPE_CONVERTER.get(type);
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

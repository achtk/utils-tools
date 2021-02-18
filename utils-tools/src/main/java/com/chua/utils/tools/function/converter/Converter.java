package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.function.converter.definition.TypeConversionDefinition;
import com.chua.utils.tools.function.converter.definition.VoidConversionDefinition;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.chua.utils.tools.util.ClassUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 转换器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public class Converter {

    private static final TypeConverter<?> VOID_TYPE_CONVERTER = new VoidTypeConverter();
    private static final TypeConversionDefinition<?, ?> VOID_TYPE_CONVERTER_DEFINITION = new VoidConversionDefinition();

    private static final Map<Class<?>, TypeConverter> TYPE_CONVERTER = ExtensionFactory.getExtensionLoader(TypeConverter.class).toPriorityMap(typeConverterExtensionClass -> typeConverterExtensionClass.getObj().getType());
    private static final Table<Class<?>, Class<?>, TypeConversionDefinition> TYPE_CONVERTER_DEFINITION = ExtensionFactory.getExtensionLoader(TypeConversionDefinition.class).stream()
            .map(entry -> {
                ExtensionClass<TypeConversionDefinition> value = entry.getValue();
                TypeConversionDefinition typeConversionDefinition = value.getObj();
                Type[] actualTypeArguments = ClassUtils.getActualTypeArguments(typeConversionDefinition.getClass(), TypeConversionDefinition.class);
                HashBasedTable<Class<?>, Class<?>, TypeConversionDefinition> table = HashBasedTable.create();
                if (actualTypeArguments.length > 1) {
                    table.put((Class<?>) actualTypeArguments[0], (Class<?>) actualTypeArguments[1], typeConversionDefinition);
                }
                return table;
            }).reduce(HashBasedTable.create(), (objectObjectObjectHashBasedTable, objectObjectObjectHashBasedTable2) -> {
                objectObjectObjectHashBasedTable.putAll(objectObjectObjectHashBasedTable2);
                return objectObjectObjectHashBasedTable;
            });
    private static final Map<Class<?>, TypeConverter> TYPE_DEFINITION_CONVERTER = ExtensionFactory.getExtensionLoader(TypeConverter.class).toPriorityMap(typeConverterExtensionClass -> typeConverterExtensionClass.getObj().getType());

    /**
     * 获取类型转换器
     *
     * @param type 类型
     * @return 类型转化器
     * @see TypeConverter
     * @since 1.0.0
     */
    public static TypeConverter getTypeConverter(Class<?> type) {
        if (null == type) {
            return VOID_TYPE_CONVERTER;
        }

        TypeConverter typeConverter = TYPE_CONVERTER.get(type);
        if (null != typeConverter) {
            return typeConverter;
        }

        return TYPE_CONVERTER.values().stream().filter(converter -> {
            return converter.getType().isAssignableFrom(type);
        }).findFirst().orElse(VOID_TYPE_CONVERTER);
    }

    /**
     * 获取类型定义
     *
     * @param type 类型
     * @return 类型转化器
     * @see TypeConverter
     * @since 1.0.0
     */
    public static <T> TypeConversionDefinition<Object, T> getTypeConversionDefinition(Class<?> type, Class<T> target) {
        TypeConversionDefinition typeConversionDefinition = TYPE_CONVERTER_DEFINITION.get(type, target);
        if (null == typeConversionDefinition) {
            Map<Class<?>, TypeConversionDefinition> column = TYPE_CONVERTER_DEFINITION.column(type);
            for (Class<?> aClass : column.keySet()) {
                if (aClass.isAssignableFrom(target)) {
                    return column.get(aClass);
                }
            }
        }
        return null == typeConversionDefinition ? (TypeConversionDefinition<Object, T>) VOID_TYPE_CONVERTER_DEFINITION : typeConversionDefinition;
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
        if (null == target) {
            return null;
        }

        if (target.isAssignableFrom(value.getClass())) {
            return (T) value;
        }

        TypeConverter typeConverter = getTypeConverter(target);
        return null == typeConverter ? null : (T) typeConverter.convert(value);
    }

    /**
     * 注入类型转化器
     *
     * @param typeConverter 类型转化器
     */
    public static void addTypeConverter(final TypeConverter typeConverter) {
        if (null == typeConverter) {
            return;
        }
        TYPE_CONVERTER.put(typeConverter.getType(), typeConverter);
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

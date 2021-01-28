package com.chua.utils.tools.manager.parser.description;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.function.converter.TypeConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 字段描述
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
@Getter
@Setter
@NoArgsConstructor
public class FieldDescription<T> {


    /**
     * 解析类
     */
    private Class<T> belongingClass;
    /**
     * 解析对象
     */
    private T entity;
    /**
     * 字段
     */
    private Field field;
    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段类型
     */
    private Class<?> type;
    /**
     * 阶级
     */
    private Class<?> declaringClass;
    /**
     * 修饰类型
     */
    private int modifiers;
    /**
     * 注解
     */
    private Annotation[] annotations;


    public FieldDescription(T entity, Field field) {
        this.entity = entity;
        this.field = field;
        this.setField(field);
    }

    /**
     * 设置字段
     *
     * @param field 字段
     */
    public void setField(Field field) {
        this.field = field;
        this.name = field.getName();
        this.type = field.getType();
        this.declaringClass = field.getDeclaringClass();
        this.modifiers = field.getModifiers();
        this.annotations = field.getAnnotations();
    }

    /**
     * 设置字段值
     *
     * @param value 值
     * @return 字段值
     */
    public Object set(Object value) {
        if (null == value) {
            return null;
        }
        T newEntity = getNewEntity();
        if (null == newEntity) {
            return null;
        }
        field.setAccessible(true);
        TypeConverter<Object> typeConverter = EmptyOrBase.getTypeConverter(type);
        try {
            if (null != typeConverter) {
                Object convert = typeConverter.convert(value);
                field.set(newEntity, convert);
                return convert;
            }
            field.set(newEntity, value);
            return value;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取字段值
     *
     * @param field  字段
     * @param entity 对象
     * @return 字段值
     */
    public static Object get(final Field field, Object entity) {
        return get(field, entity, Object.class);
    }

    /**
     * 获取字段值
     *
     * @param field      字段
     * @param entity     对象
     * @param returnType 返回类型
     * @return 字段值
     */
    public static <T> T get(final Field field, Object entity, Class<T> returnType) {
        Object value = new FieldDescription<>(entity, field).get();
        return null == value || returnType == null || returnType.isAssignableFrom(value.getClass()) ? (T) value : null;
    }

    /**
     * 获取字段值
     *
     * @return 字段值
     */
    public Object get() {
        T newEntity = getNewEntity();
        if (null == newEntity) {
            return null;
        }

        field.setAccessible(true);
        try {
            return field.get(newEntity);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 获取对象
     *
     * @return 对象
     */
    private T getNewEntity() {
        if (entity != null) {
            return entity;
        } else if (!belongingClass.isInterface()) {
            return ClassHelper.safeForObject(belongingClass);
        }
        return null;
    }
}

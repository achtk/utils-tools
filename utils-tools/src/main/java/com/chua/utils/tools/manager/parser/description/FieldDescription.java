package com.chua.utils.tools.manager.parser.description;

import lombok.Getter;
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
public class FieldDescription<T> {
    /**
     * 类
     */
    private Class<T> tClass;
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
}

package com.chua.utils.tools.manager.parser.description;

import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 方法描述
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
@Getter
@Setter
public class MethodDescription<T> {
    /**
     * 类
     */
    private Class<T> tClass;
    /**
     * 方法
     */
    private Method method;
    /**
     * 方法名
     */
    private String name;
    /**
     * 方法类型
     */
    private Class<?> type;
    /**
     * 请求参数类型
     */
    private Class<?>[] paramTypes;
    /**
     * 方法注解
     */
    private Annotation[] annotations;
    /**
     * 异常类型
     */
    private Class<?>[] exceptionTypes;

    /**
     * 设置方法
     *
     * @param method 方法
     */
    public void setMethod(Method method) {
        this.method = method;
        this.name = method.getName();
        this.type = method.getReturnType();
        this.paramTypes = method.getParameterTypes();
        this.annotations = method.getDeclaredAnnotations();
        this.exceptionTypes = method.getExceptionTypes();
    }
}

package com.chua.utils.tools.manager.parser.description;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.named.NamedHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class MethodDescription<T> {
    /**
     * 类
     */
    private Class<T> belongingClass;
    /**
     * 解析对象
     */
    private T entity;
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
     * 请求参数个数
     */
    private int paramSize;
    /**
     * 方法注解
     */
    private Annotation[] annotations;
    /**
     * 异常类型
     */
    private Class<?>[] exceptionTypes;

    public MethodDescription(T entity, Class<?>... paramTypes) {
        this.entity = entity;
        this.paramTypes = paramTypes;
    }

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
        this.paramSize = paramTypes.length;
    }

    /**
     * 执行方法
     *
     * @param params 参数
     * @return Object
     */
    public Object invoke(Object... params) {
        return invoke(params, Object.class);
    }

    /**
     * 执行方法
     *
     * @param params     参数
     * @param returnType 返回类型
     * @return R
     */
    public <R> R invoke(Object[] params, Class<R> returnType) {
        T newEntity = null;
        if (entity != null) {
            newEntity = entity;
        } else if (!belongingClass.isInterface()) {
            newEntity = ClassHelper.safeForObject(belongingClass);
        }
        if (null == newEntity) {
            return null;
        }

        if (null == params) {
            params = new Object[0];
        }

        if (paramSize != params.length) {
            return null;
        }

        Object[] objects = ClassHelper.doTypeConsistent(paramTypes, params);
        if (null == objects) {
            return null;
        }
        try {
            Object invoke = method.invoke(newEntity, objects);
            return null != invoke && returnType.isAssignableFrom(invoke.getClass()) ? (R) invoke : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * set方法
     *
     * @param name
     */
    public void setSetName(String name) {
        String name1 = "set" + NamedHelper.firstUpperCase(name);
        this.setName(name1);
    }

    /**
     * get方法
     *
     * @param name
     */
    public void setGetName(String name) {
        String name1 = "get" + NamedHelper.firstUpperCase(name);
        this.setName(name1);
    }

    /**
     * 存在方法
     *
     * @param name
     * @return
     */
    public boolean existSetMethod(String name) {
        setSetName(name);
        return existMethod(this.name);
    }

    /**
     * 存在方法
     *
     * @param name
     * @return
     */
    public boolean existGetMethod(String name) {
        setGetName(name);
        return existMethod(this.name);
    }

    /**
     * 存在方法
     *
     * @return
     */
    public boolean existMethod() {
        return existMethod(this.name);
    }

    /**
     * 存在方法
     *
     * @param name
     * @return
     */
    public boolean existMethod(String name) {
        if (null != method && method.getName().equals(name)) {
            return true;
        }
        Class<?> aClass = entity.getClass();
        try {
            Method method = aClass.getDeclaredMethod(this.name, this.paramTypes);
            setMethod(method);
            return null != method;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}

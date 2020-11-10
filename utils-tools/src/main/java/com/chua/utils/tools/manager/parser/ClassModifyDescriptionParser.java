package com.chua.utils.tools.manager.parser;

import com.chua.utils.tools.manager.parser.description.ModifyDescription;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 类修改修饰器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public interface ClassModifyDescriptionParser<T> {

    /**
     * 添加注解
     *
     * @param annotations 注解
     */
    void addAnnotation(Annotation... annotations);

    /**
     * 添加方法
     *
     * @param method      完整方法(不包含泛型)
     * @param annotations 注解
     */
    void addMethod(String method, Annotation... annotations);

    /**
     * 添加父类
     *
     * @param aClass 父类
     */
    void addSuper(Class<?> aClass);

    /**
     * 添加字段
     *
     * @param field       完整字段(不包含泛型)
     * @param annotations 注解
     */
    void addField(String field, Annotation... annotations);

    /**
     * 给字段添加注解
     *
     * @param fieldName   字段名称
     * @param annotations 注解
     */
    void addFieldsAnnotations(String fieldName, Annotation... annotations);

    /**
     * 添加接口
     *
     * @param interfaces 接口(不包含泛型)
     */
    void addInterface(Class<?>... interfaces);

    /**
     * 给方法添加注解
     *
     * @param methodName  方法
     * @param annotations 注解
     */
    void addMethodAnnotations(String methodName, Annotation... annotations);

    /**
     * 替换方法
     *
     * @param methodName 方法名称
     * @param method     完整方法(不包含泛型)
     */
    void replaceMethod(String methodName, String method);

    /**
     * 替换方法
     *
     * @param method     方法
     * @param methodBody 完整方法(不包含泛型)
     */
    void replaceMethod(Method method, String methodBody);

    /**
     * 获取类
     *
     * @return ModifyDescription
     * @throws Exception Exception
     * @see ModifyDescription
     */
    ModifyDescription toClass() throws Exception;
}

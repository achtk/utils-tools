package com.chua.utils.tools.manager.parser;

import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.manager.parser.description.ModifyDescription;
import javassist.CtMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import static com.chua.utils.tools.constant.StringConstant.PRIVATE;
import static com.chua.utils.tools.constant.StringConstant.PUBLIC;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_EMPTY;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_SEMICOLON;

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
     * 设置名称
     *
     * @param name 名称
     */
    void setName(String name);

    /**
     * 添加方法
     *
     * @param method      完整方法(不包含泛型)
     * @param annotations 注解
     */
    void addMethod(String method, Annotation... annotations);

    /**
     * 添加方法
     *
     * @param methodName  方法名称
     * @param methodType  方法类型
     * @param paramTypes  方法参数
     * @param methodBody  方法体
     * @param annotations 注解
     */
    default void addMethod(String methodName, Class<?> methodType, Class<?>[] paramTypes, String methodBody, Annotation... annotations) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PUBLIC).append(" ");
        stringBuilder.append(methodType.getName()).append(" ");
        stringBuilder.append(methodName).append(" ");
        stringBuilder.append("(");
        if (null != paramTypes && paramTypes.length > 0) {
            String params = "";
            AtomicInteger atomicInteger = new AtomicInteger();
            for (Class<?> paramType : paramTypes) {
                params += "," + paramType.getName() + " " + paramType.getSimpleName() + atomicInteger.getAndIncrement();
            }
            stringBuilder.append(params.substring(1));
        }
        stringBuilder.append(") {");
        stringBuilder.append(methodBody);
        stringBuilder.append("}");

        addMethod(stringBuilder.toString(), annotations);
    }

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
     * 添加字段Get方法
     *
     * @param fieldName   字段名称
     * @param annotations 注解
     */
    void addFieldGetter(String fieldName, Annotation... annotations);

    /**
     * 添加字段Get方法
     *
     * @param fieldName   字段名称
     * @param annotations 注解
     */
    void addFieldSetter(String fieldName, Annotation... annotations);

    /**
     * 添加字段
     *
     * @param fieldName   字段名称
     * @param fieldType   字段类型
     * @param annotations 注解
     */
    default void addField(String fieldName, Class<?> fieldType, Annotation... annotations) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PRIVATE).append(" ");
        stringBuilder.append(fieldType.getName()).append(" ").append(fieldName).append(SYMBOL_SEMICOLON);

        addField(stringBuilder.toString(), annotations);
    }

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
    ModifyDescription<T> toClass() throws Exception;
}

package com.chua.utils.tools.bean.part;

import com.chua.utils.tools.constant.SymbolConstant;
import com.chua.utils.tools.resource.template.ResourceTemplate;
import com.chua.utils.tools.util.ArrayUtils;
import com.chua.utils.tools.util.ClassUtils;
import com.chua.utils.tools.util.CollectionUtils;
import com.chua.utils.tools.util.StringUtils;
import com.google.common.base.Strings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * bean part
 * <p>bean各个部分数据</p>
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/8
 */
public interface BeanPart<T> {

    /**
     * 代理
     *
     * @param object 对象
     * @return 代理
     */
    static <T> BeanPart<T> of(T object) {
        return new StandardBeanPart<>(object);
    }

    /**
     * 获取类泛型
     *
     * @return 除过滤的接口
     */
    default List<Class<?>> getTypeParameters() {
        if (null == toClass()) {
            return Collections.emptyList();
        }
        TypeVariable<Class<T>>[] typeParameters = toClass().getTypeParameters();
        List<Class<?>> result = new ArrayList<>(typeParameters.length);

        for (TypeVariable<Class<T>> typeParameter : typeParameters) {
            result.add(typeParameter.getGenericDeclaration());
        }
        return result;
    }

    /**
     * 获取所有接口
     *
     * @param filter 过滤的接口
     * @return 除过滤的接口
     */
    default List<Class<?>> getInterfaces(Class<?> filter) {
        List<Class<?>> interfaces = getInterfaces();
        if (CollectionUtils.isEmpty(interfaces)) {
            return Collections.emptyList();
        }
        return interfaces.stream().filter(item -> !item.isAssignableFrom(filter)).collect(Collectors.toList());
    }

    /**
     * 获取所有接口
     *
     * @param predicate 过滤的接口
     * @return 除过滤的接口
     */
    default List<Class<?>> getInterfaces(Predicate<Class<?>> predicate) {
        List<Class<?>> interfaces = getInterfaces();
        if (CollectionUtils.isEmpty(interfaces)) {
            return Collections.emptyList();
        }
        return interfaces.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 获取所有接口
     *
     * @return 所有接口
     */
    default List<Class<?>> getInterfaces() {
        if (null == toClass()) {
            return Collections.emptyList();
        }
        return Arrays.asList(toClass().getInterfaces());
    }

    /**
     * 获取所有方法
     *
     * @return 所有方法
     */
    default List<Method> getLocalMethods() {
        return ClassUtils.getLocalMethods(toClass());
    }

    /**
     * 获取所有方法名称
     *
     * @return 所有方法
     */
    default List<String> getLocalMethodNames() {
        return Optional.ofNullable(getLocalMethods()).orElse(Collections.emptyList()).stream().map(Method::getName).collect(Collectors.toList());
    }

    /**
     * 获取所有字段
     *
     * @return 所有字段
     */
    default List<Field> getLocalFields() {
        return ClassUtils.getLocalFields(toClass());
    }

    /**
     * 获取所有字段名称
     *
     * @return 所有字段
     */
    default List<String> getLocalFieldNames() {
        return Optional.ofNullable(getLocalFields()).orElse(Collections.emptyList()).stream().map(Field::getName).collect(Collectors.toList());
    }

    /**
     * 获取所有字段
     *
     * @return 所有字段
     */
    default List<Field> getFields() {
        return ClassUtils.getFields(toClass());
    }

    /**
     * 获取所有方法
     *
     * @return 所有方法
     */
    default List<Method> getMethods() {
        return ClassUtils.getMethods(toClass());
    }

    /**
     * 获取子类
     *
     * @return 子类
     */
    @SuppressWarnings("ALL")
    default List<Class<? extends T>> getSubClasses() {
        if (null == toClass()) {
            return Collections.emptyList();
        }

        Class<T> objectClass = toClass();
        if (ClassUtils.isObject(objectClass)) {
            return Collections.emptyList();
        }
        ResourceTemplate resourceTemplate = new ResourceTemplate();
        List<Class<?>> subOfType = resourceTemplate.getSubOfType(objectClass);
        return null == subOfType ? Collections.emptyList() : subOfType.stream().map(aClass -> {
            if (objectClass.isAssignableFrom(aClass)) {
                return (Class<T>) aClass;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    default String getName() {
        return null == toClass() ? SymbolConstant.SYMBOL_EMPTY : toClass().getName();
    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    default String getSimpleName() {
        return null == toClass() ? SymbolConstant.SYMBOL_EMPTY : toClass().getSimpleName();
    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    Class<T> toClass();

    /**
     * 获取子类
     *
     * @return 子类
     */
    default List<? extends T> getSubEntity() {
        List<Class<? extends T>> subClasses = getSubClasses();
        if (null == subClasses) {
            return Collections.emptyList();
        }
        return subClasses.stream().map(ClassUtils::forObject).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 获取字段值
     *
     * @param field 字段值
     * @return 结果
     */
    Object getFieldValue(Field field);

    /**
     * 获取字段
     *
     * @param fieldName 字段名
     * @return 满足条件的方法
     */
    default Object getFieldValue(String fieldName) {
        return getFieldValue(getField(fieldName, null));
    }

    /**
     * 设置字段值
     *
     * @param field 字段值
     * @param value 值
     */
    void setFieldValue(Field field, Object value);

    /**
     * 执行方法
     *
     * @param method 方法
     * @param params 参数
     * @return 结果
     */
    Object invoke(Method method, Object... params);

    /**
     * 获取方法
     *
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return 满足条件的方法
     */
    default Method getMethod(String methodName, Class<?>... paramTypes) {
        if (Strings.isNullOrEmpty(methodName)) {
            return null;
        }

        List<Method> methods = getLocalMethods();
        if (CollectionUtils.isEmpty(methods)) {
            return null;
        }
        return methods.stream().filter(item -> {
            if (null == paramTypes) {
                return item.getName().equals(methodName);
            }
            return item.getName().equals(methodName) && ClassUtils.isMethod(item, paramTypes);
        }).findFirst().orElse(null);
    }

    /**
     * 通过注解获取方法
     *
     * @param annotationType 注解类型
     * @return 满足条件的方法
     */
    default List<Method> getMethodsByAnnotation(Class<? extends Annotation> annotationType) {
        List<Method> methods = getLocalMethods();
        if (null == annotationType || CollectionUtils.isEmpty(methods)) {
            return Collections.emptyList();
        }
        return methods.stream().filter(item -> item.getDeclaredAnnotation(annotationType) != null).collect(Collectors.toList());
    }

    /**
     * 通过参数注解获取方法
     *
     * @param annotationType 注解类型
     * @return 满足条件的方法
     */
    default List<Method> getMethodsByParamAnnotation(Class<? extends Annotation> annotationType) {
        List<Method> methods = getLocalMethods();
        if (null == annotationType || CollectionUtils.isEmpty(methods)) {
            return Collections.emptyList();
        }
        return methods.stream().filter(item -> {
            boolean hasAnnotation = false;
            Annotation[][] parameterAnnotations = item.getParameterAnnotations();
            loop:
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation.getClass().isAssignableFrom(annotationType)) {
                        hasAnnotation = true;
                        break loop;
                    }
                }
            }
            return hasAnnotation;
        }).collect(Collectors.toList());
    }

    /**
     * 获取方法
     *
     * @param predicate 过滤
     * @return 满足条件的方法
     */
    default List<Method> getLocalMethods(Predicate<Method> predicate) {
        List<Method> methods = getLocalMethods();
        if (null == predicate || CollectionUtils.isEmpty(methods)) {
            return Collections.emptyList();
        }
        return methods.stream().filter(item -> predicate.test(item)).collect(Collectors.toList());
    }

    /**
     * 是否存在方法
     *
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return 存在方法返回true
     */
    default boolean hasMethod(String methodName, Class<?>... paramTypes) {
        return getMethod(methodName, paramTypes) != null;
    }

    /**
     * 获取所有方法
     *
     * @param methodName 方法名称
     * @return 除过滤的方法
     */
    default List<Method> getLocalMethods(String methodName) {
        if (Strings.isNullOrEmpty(methodName)) {
            return Collections.emptyList();
        }
        List<Method> methods = getLocalMethods();
        if (CollectionUtils.isEmpty(methods)) {
            return Collections.emptyList();
        }
        return methods.stream().filter(item -> !item.equals(methodName)).collect(Collectors.toList());
    }

    /**
     * 是否有字段
     *
     * @param name 字段名
     * @return 满足条件的方法
     */
    default boolean hasField(String name) {
        List<Field> fields = getLocalFields();
        if (Strings.isNullOrEmpty(name) || CollectionUtils.isEmpty(fields)) {
            return false;
        }
        return getField(name) != null;
    }

    /**
     * 通过注解获取字段
     *
     * @param annotationType 注解类型
     * @return 满足条件的字段
     */
    default List<Field> getFieldsByAnnotation(Class<? extends Annotation> annotationType) {
        List<Field> fields = getLocalFields();
        if (null == annotationType || CollectionUtils.isEmpty(fields)) {
            return Collections.emptyList();
        }

        return fields.stream().filter(field -> null != field.getDeclaredAnnotation(annotationType)).collect(Collectors.toList());
    }

    /**
     * 获取字段
     *
     * @param predicate 过滤
     * @return 满足条件的方法
     */
    default List<Field> getLocalFields(Predicate<Field> predicate) {
        List<Field> fields = getLocalFields();
        if (null == predicate || CollectionUtils.isEmpty(fields)) {
            return Collections.emptyList();
        }
        return fields.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 获取字段
     *
     * @param fieldName 字段名
     * @return 满足条件的方法
     */
    default Field getField(String fieldName) {
        return getField(fieldName, null);
    }

    /**
     * 获取字段
     *
     * @param fieldName 字段名
     * @return 满足条件的方法
     */
    default List<Field> wildcardMatchField(String fieldName) {
        if (Strings.isNullOrEmpty(fieldName)) {
            return null;
        }

        List<Field> fields = getLocalFields();
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }
        return fields.stream().filter(item -> StringUtils.wildcardMatch(item.getName(), fieldName)).collect(Collectors.toList());
    }

    /**
     * 获取字段
     *
     * @param fieldName 字段名
     * @param paramType 参数类型
     * @return 满足条件的方法
     */
    default Field getField(String fieldName, Class<?> paramType) {
        if (Strings.isNullOrEmpty(fieldName)) {
            return null;
        }

        List<Field> fields = getLocalFields();
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }
        return fields.stream().filter(item -> {
            if (null == paramType) {
                return item.getName().equals(fieldName);
            }
            return item.getName().equals(fieldName) && item.getType().isAssignableFrom(paramType);
        }).findFirst().orElse(null);
    }

    /**
     * 获取泛型类型
     *
     * @param includes 条件
     * @return 满足条件的类型
     */
    default List<Type> getActualTypeArguments(Class<?>... includes) {
        if (null == toClass()) {
            return Collections.emptyList();
        }
        return ArrayUtils.newArrays(ClassUtils.getActualTypeArguments(toClass(), includes));
    }

    /**
     * 获取字段注解
     *
     * @param fieldName 字段名称
     * @return 注解
     */
    default List<Annotation> getFieldAnnotation(String fieldName) {
        Field field = getField(fieldName);
        return null == field ? Collections.emptyList() : Arrays.asList(field.getDeclaredAnnotations());
    }
}

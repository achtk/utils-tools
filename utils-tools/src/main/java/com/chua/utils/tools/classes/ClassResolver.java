package com.chua.utils.tools.classes;

import com.chua.utils.tools.function.Converter;
import com.chua.utils.tools.function.intercept.MethodIntercept;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 类解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/24
 */
public interface ClassResolver {
    /**
     * 获取注解
     *
     * @return 注解
     */
    Map<String, Annotation> annotations();

    /**
     * 自动转配
     *
     * @param entity    对象
     * @param prefix    前缀
     * @param converter 转化器
     * @param <T>       T
     * @return 对象
     */
    <T> T automaticAssembly(T entity, String prefix, Converter<String, Object> converter);

    /**
     * 自动转配
     *
     * @param prefix    前缀
     * @param converter 转化器
     * @param <T>       T
     * @return T
     */
    <T> T automaticAssembly(String prefix, Converter<String, Object> converter);

    /**
     * 是否是type子类
     *
     * @param type 类
     * @return 是否是type子类
     */
    boolean isAssignableFrom(Class<?> type);

    /**
     * 查询子类
     *
     * @return 子类
     */
    Set<? extends Class> findSubType();

    /**
     * 代理方法
     *
     * @param methodIntercept 代理
     * @return 对象
     */
    Object proxy(final MethodIntercept methodIntercept);

    /**
     * 实例化
     *
     * @param <T> T
     * @return T 对象
     * @throws Exception Exception
     */
    <T> T newInstance() throws Exception;

    /**
     * 查询子类
     *
     * @return 子类
     */
    default Set<?> findSubObject() {
        if (null == findSubType()) {
            return Collections.emptySet();
        }
        Set<Object> result = new HashSet<>();
        findSubType().parallelStream().forEach((Consumer<Class>) aClass -> {
            Object object = ClassHelper.forObject(aClass);
            if (null == object) {
                return;
            }
            result.add(object);
        });

        return result;
    }

    /**
     * 是否包含注解
     *
     * @param annotationType 注解
     * @return 是否包含注解
     */
    default boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        return getAnnotation(annotationType) != null;
    }

    /**
     * 获取注解
     *
     * @param annotationType 注解
     * @return 注解
     */
    default <T> T getAnnotation(Class<T> annotationType) {
        if (null == annotations() || null == annotationType) {
            return null;
        }
        return (T) annotations().get(annotationType.getName());
    }

}

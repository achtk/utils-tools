package com.chua.utils.tools.classes;

import com.chua.utils.tools.function.Converter;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.function.intercept.MethodIntercept;

import java.lang.annotation.Annotation;
import java.util.*;
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
     * @return
     */
    Map<String, Annotation> annotations();

    /**
     * 自动转配
     *
     * @param entity    对象
     * @param prefix    前缀
     * @param converter 转化器
     * @param <T>
     * @return
     */
    <T> T automaticAssembly(T entity, String prefix, Converter<String, Object> converter);
    /**
     * 自动转配
     *
     * @param prefix    前缀
     * @param converter 转化器
     * @param <T>
     * @return
     */
    <T> T automaticAssembly(String prefix, Converter<String, Object> converter);

    /**
     * 是否是type子类
     *
     * @param type
     * @return
     */
    boolean isAssignableFrom(Class<?> type);

    /**
     * 查询子类
     *
     * @return
     */
    Set<? extends Class> findSubType();

    /**
     * 代理方法
     *
     * @param methodIntercept 代理
     * @return
     */
    Object proxy(MethodIntercept methodIntercept);

    /**
     * 实例化
     *
     * @param <T>
     * @return
     */
    <T> T newInstance() throws Exception;

    /**
     * 查询子类
     *
     * @return
     */
    default Set<?> findSubObject() {
        if (null == findSubType()) {
            return Collections.emptySet();
        }
        Set<Object> result = new HashSet<>();
        findSubType().parallelStream().forEach(new Consumer<Class>() {
            @Override
            public void accept(Class aClass) {
                Object object = ClassHelper.forObject(aClass);
                if (null == object) {
                    return;
                }
                result.add(object);
            }
        });

        return result;
    }

    /**
     * 是否包含注解
     *
     * @param annotationType 注解
     * @return
     */
    default boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        return getAnnotation(annotationType) != null;
    }

    /**
     * 获取注解
     *
     * @param annotationType 注解
     * @return
     */
    default <T> T getAnnotation(Class<T> annotationType) {
        if (null == annotations() || null == annotationType) {
            return null;
        }
        return (T) annotations().get(annotationType.getName());
    }

}

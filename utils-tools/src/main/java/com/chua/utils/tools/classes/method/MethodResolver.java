package com.chua.utils.tools.classes.method;

import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

/**
 * 方法解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/24
 */
public interface MethodResolver {
    /**
     * 执行方法
     *
     * @param methodName 方法名称
     * @param args       方法参数
     * @return
     */
    Object invoke(String methodName, Object... args) throws Throwable;
    /**
     * 执行方法
     *
     * @param methodName 方法名称
     * @param args       方法参数
     * @return
     */
    default Object safeInvoke(String methodName, Object... args) {
        try {
            return invoke(methodName, args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有方法
     *
     * @return
     */
    Set<Method> methods();

    /**
     * 获取方法注解
     *
     * @param method 方法
     * @return
     */
    Set<String> findAnnotation(Method method);

    /**
     * 获取方法注解
     *
     * @param annotationType 注解类型
     * @return
     */
    default List<Method> findMethodByAnnotation(Class<? extends Annotation> annotationType) {
        if (null == methods() || null == annotationType) {
            return Collections.emptyList();
        }
        List<Method> result = new ArrayList<>();
        methods().parallelStream().forEach(new Consumer<Method>() {
            @Override
            public void accept(Method method) {
                Set<String> annotation = findAnnotation(method);
                if (!annotation.contains(annotationType.getName())) {
                    return;
                }
                result.add(method);
            }
        });
        return result;
    }

    /**
     * 查找方法
     *
     * @param methodName 方法名称
     * @return
     */
    default Set<Method> findMethod(String methodName) {
        if (null == methods() || Strings.isNullOrEmpty(methodName)) {
            return null;
        }
        Set<Method> result = new HashSet<>();
        methods().parallelStream().forEach(new Consumer<Method>() {
            @Override
            public void accept(Method method) {
                if (!method.getName().equals(methodName)) {
                    return;
                }
                result.add(method);
            }
        });
        return result;
    }

    /**
     * 查找方法
     *
     * @param methodName 方法名称
     * @param paramsType 参数类型
     * @return
     */
    default Method findMethod(String methodName, Class<?>[] paramsType) {
        if (null == methods() || Strings.isNullOrEmpty(methodName)) {
            return null;
        }
        Set<Method> result = new HashSet<>();
        methods().parallelStream().forEach(new Consumer<Method>() {
            @Override
            public void accept(Method method) {
                if (!method.getName().equals(methodName)) {
                    return;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                boolean paramMatcher = true;
                typeloop:
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    if (!parameterType.isAssignableFrom(paramsType[i])) {
                        paramMatcher = false;
                        break typeloop;
                    }
                }

                if (paramMatcher) {
                    result.add(method);
                }
            }
        });
        return result.size() == 1 ? FinderHelper.firstElement(result) : null;
    }

    /**
     * 查找方法
     *
     * @param paramsType 参数类型
     * @return
     */
    default List<Method> findMethod(Class<?>[] paramsType) {
        if (null == methods()) {
            return null;
        }

        return findMethod(new Filter<Method>() {
            @Override
            public boolean matcher(Method item) {
                Class<?>[] parameterTypes = item.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    if (parameterType.isAssignableFrom(paramsType[i])) {
                        continue;
                    }
                    return false;
                }
                return true;
            }
        });
    }

    /**
     * 查找方法
     *
     * @param filter 过滤方法
     * @return
     */
    default List<Method> findMethod(Filter<Method> filter) {
        if (null == methods() || null == filter) {
            return null;
        }
        List<Method> methodList = new ArrayList<>();
        methods().parallelStream().forEach(new Consumer<Method>() {
            @Override
            public void accept(Method method) {
                boolean matcher = filter.matcher(method);
                if (!matcher) {
                    return;
                }
                methodList.add(method);
            }
        });
        return methodList;
    }
}

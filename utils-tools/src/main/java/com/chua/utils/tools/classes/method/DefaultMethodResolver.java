package com.chua.utils.tools.classes.method;

import com.chua.utils.tools.cache.ConcurrentSetCacheProvider;
import com.chua.utils.tools.cache.MultiCacheProvider;
import com.chua.utils.tools.classes.ClassHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 方法解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/24
 */
public class DefaultMethodResolver implements MethodResolver {
    private Object obj;
    private Class sourceClass;
    private volatile Object proxyObj;
    /**
     * 注解-方法
     */
    protected MultiCacheProvider<Method, String> methodAnnotationCache = new ConcurrentSetCacheProvider();
    protected CopyOnWriteArraySet<Method> methods = new CopyOnWriteArraySet();

    public DefaultMethodResolver(Object obj) {
        this.obj = obj;
        this.proxyObj = obj;
        this.initial();
    }

    /**
     * 初始化
     */
    private void initial() {
        if (null == obj) {
            return;
        }
        this.sourceClass = ClassHelper.getClass(obj);
        Method[] methods = sourceClass.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                methodAnnotationCache.add(method, annotation.annotationType().getName());
            }
            this.methods.add(method);
        }
    }

    @Override
    public Object invoke(String methodName, Object... args) throws Throwable {
        Class<?>[] paramType = ClassHelper.toParamType(args);
        Method method = findMethod(methodName, paramType);
        ClassHelper.makeAccessible(method);
        return null == method ? null : method.invoke(proxyObj, args);
    }

    @Override
    public Set<Method> methods() {
        return this.methods;
    }

    @Override
    public Set<String> findAnnotation(Method method) {
        if (methodAnnotationCache.container(method)) {
            return methodAnnotationCache.get(method);
        }
        Annotation[] annotations = method.getDeclaredAnnotations();
        Set<String> result = new HashSet<>();
        for (Annotation annotation : annotations) {
            result.add(annotation.annotationType().getName());
        }
        return result;
    }

}

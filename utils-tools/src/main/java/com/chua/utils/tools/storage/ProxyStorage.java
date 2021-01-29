package com.chua.utils.tools.storage;

import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.chua.utils.tools.util.ClassUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class ProxyStorage {
    /**
     * 代理
     *
     * @param <T>             类型
     * @param entity          对象
     * @param methodIntercept 方法拦截器
     * @return 代理类
     */
    public static <T> T run(Class<T> entity, final MethodIntercept methodIntercept) {
        return (T) run(ClassUtils.forObject(entity), methodIntercept);
    }

    /**
     * 代理
     *
     * @param <T>             类型
     * @param entity          对象
     * @param methodIntercept 方法拦截器
     * @return 代理类
     */
    public static <T> Object run(T entity, final MethodIntercept methodIntercept) {
        if (null == entity) {
            return null;
        }

        if (entity instanceof Class) {
            entity = (T) ClassUtils.forObject((Class) entity);
        }

        if (entity instanceof String) {
            entity = (T) ClassUtils.forObject((String) entity);
        }

        Class<?> aClass = entity.getClass();
        if (aClass.isInterface()) {
            return createInterfaceProxy(aClass, entity, methodIntercept);
        }
        return createEntityProxy(aClass, entity, methodIntercept);

    }

    /**
     * 接口代理
     *
     * @param <T>             类型
     * @param aClass          接口类
     * @param entity          实体
     * @param methodIntercept 方法拦截器
     * @return 代理
     */
    @SuppressWarnings("ALL")
    private static <T> T createInterfaceProxy(Class<?> aClass, Object entity, MethodIntercept methodIntercept) {
        return (T) Proxy.newProxyInstance(aClass.getClassLoader(), new Class[]{aClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return methodIntercept.invoke(entity, method, args, proxy);
            }
        });
    }

    /**
     * 实体代理
     *
     * @param <T>             类型
     * @param aClass          类
     * @param entity          实体
     * @param methodIntercept 方法拦截器
     * @return 代理
     */
    private static <T> T createEntityProxy(Class<?> aClass, Object entity, MethodIntercept methodIntercept) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(aClass);
        enhancer.setUseCache(true);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> methodIntercept.invoke(entity, method, args, proxy));
        return (T) enhancer.create();
    }
}

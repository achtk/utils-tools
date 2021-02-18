package com.chua.utils.tools.manager.builder;

import com.chua.utils.tools.function.intercept.MethodIntercept;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 缓存策略构造器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public class StandardProxyStrategyBuilder<T> implements ProxyStrategyBuilder<T>, StrategyBuilder<T> {
    private MethodIntercept methodIntercept;

    @Override
    public ProxyCreateStrategyBuilder<T> proxy(MethodIntercept methodIntercept) {
        this.methodIntercept = methodIntercept;
        return new ProxyCreateStrategyBuilder<T>() {
            @Override
            public T create(T entity) {
                return StandardProxyStrategyBuilder.this.create(entity);
            }
        };
    }

    private T createClassProxy(Class entity) {
        if (entity.isInterface()) {
            return (T) Proxy.newProxyInstance(entity.getClassLoader(), new Class[]{entity}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return methodIntercept.invoke(proxy, method, args, null);
                }
            });
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(entity);
        enhancer.setUseCache(true);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> methodIntercept.invoke(obj, method, args, proxy));
        return (T) enhancer.create();
    }

    @Override
    public T create(T entity) {
        Class tClass = null;
        if (!(entity instanceof Class)) {
            tClass = entity.getClass();
        } else {
            tClass = (Class) entity;
        }
        return createClassProxy(tClass);
    }
}

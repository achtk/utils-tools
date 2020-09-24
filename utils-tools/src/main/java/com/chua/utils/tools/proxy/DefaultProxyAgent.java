package com.chua.utils.tools.proxy;

import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.mapper.ProxyMapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk proxy
 * @author CH
 */
public class DefaultProxyAgent<T> extends ProxyAgent<T> {

    public DefaultProxyAgent(Class<T> source, ProxyMapper proxyMapper) {
        super(source, proxyMapper);
    }

    @Override
    public T newProxy() {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{getSource()}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ProxyMapper proxyMapper = getProxyMapper();
                if(null == proxyMapper) {
                    return ProxyMapper.intercept(proxy, method, args, proxy);
                }
                return invoker(getProxyMapper(), proxy, method, args, null);
            }
        });
    }

    @Override
    public Object invoker(ProxyMapper proxyMapper, Object obj, Method method, Object[] args, Object... proxy) throws Throwable {
        String methodName = method.getName();

        if(!proxyMapper.hasName(methodName)) {
            return ProxyMapper.intercept(obj, method, args, proxy);
        }

        MethodIntercept methodIntercept = proxyMapper.tryToGetProxy(methodName);
        if(null != methodIntercept) {
            return methodIntercept.invoke(obj, method, args, proxy);
        }
        return ProxyMapper.intercept(obj, method, args, proxy);
    }
}

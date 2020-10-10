package com.chua.utils.tools.proxy;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.loader.BalancerLoader;
import com.chua.utils.tools.loader.RotationBalancerLoader;
import com.chua.utils.tools.mapper.ProxyMapper;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * jdk proxy
 * @author CH
 */
@AllArgsConstructor
public class DefaultProxyAgent<T> implements ProxyAgent<T> {

    private ProxyMapper proxyMapper;
    private BalancerLoader balancerLoader = new RotationBalancerLoader();

    @Override
    public T newProxy(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(ClassHelper.getDefaultClassLoader(), new Class[]{tClass}, new MethodInterceptFactory());
    }

    @Override
    public Object invoker(ProxyMapper proxyMapper, Object obj, Method method, Object[] args, Object... proxy) throws Throwable {
        String methodName = method.getName();

        if(null == proxyMapper || !proxyMapper.hasName(methodName)) {
            return ProxyMapper.intercept(obj, method, args, proxy);
        }

        MethodIntercept methodIntercept = proxyMapper.tryToGetProxy(methodName, balancerLoader);
        if(null != methodIntercept) {
            return methodIntercept.invoke(obj, method, args, proxy);
        }
        return ProxyMapper.intercept(obj, method, args, proxy);
    }

    private class MethodInterceptFactory implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return invoker(proxyMapper, proxy, method, args, null);
        }
    }
}

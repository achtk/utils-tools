package com.chua.utils.tools.proxy;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.chua.utils.tools.loader.BalancerLoader;
import com.chua.utils.tools.loader.RotationBalancerLoader;
import com.chua.utils.tools.mapper.AllProxyMapper;
import com.chua.utils.tools.mapper.ProxyMapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk proxy
 * @author CH
 */
public class DefaultProxyAgent<T> implements ProxyAgent<T> {

    private ProxyMapper mapper;
    private BalancerLoader balancerLoader = new RotationBalancerLoader();

    public DefaultProxyAgent() {}
    public DefaultProxyAgent(MethodIntercept methodIntercept) {
        this.mapper = new AllProxyMapper(methodIntercept);
    }

    public DefaultProxyAgent(ProxyMapper proxyMapper, BalancerLoader balancerLoader) {
        this.mapper = proxyMapper;
        this.balancerLoader = balancerLoader;
    }

    @Override
    public T newProxy(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(ClassHelper.getDefaultClassLoader(), new Class[]{tClass}, new MethodInterceptFactory());
    }

    @Override
    public Object invoker(Object obj, Method method, Object[] args, Object proxy) throws Throwable {
        String methodName = method.getName();

        ClassHelper.methodAccessible(method);
        if(null == mapper || !mapper.hasName(methodName)) {
            return method.invoke(obj, args);
        }

        MethodIntercept methodIntercept = mapper.tryToGetProxy(methodName, balancerLoader);
        if(null != methodIntercept) {
            return methodIntercept.invoke(obj, method, args, proxy);
        }
        return method.invoke(obj, args);
    }

    private class MethodInterceptFactory implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return invoker(proxy, method, args, null);
        }
    }
}

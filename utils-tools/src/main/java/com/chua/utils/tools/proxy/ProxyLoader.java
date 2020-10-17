package com.chua.utils.tools.proxy;

import com.chua.utils.tools.exceptions.NotSupportedException;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.chua.utils.tools.loader.BalancerLoader;
import com.chua.utils.tools.mapper.ProxyMapper;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理加载器
 * @author CH
 */
public class ProxyLoader<T> {

    private ProxyAgent<T> proxyAgent;

    public ProxyLoader(BalancerLoader balancerLoader, ProxyMapper proxyMapper) {
        this.proxyAgent = new DefaultProxyAgent<T>(proxyMapper, balancerLoader);
    }

    public ProxyLoader(ProxyAgent<T> proxyAgent) {
        this.proxyAgent = proxyAgent;
    }

    /**
     * 产生代理
     * @return
     */
    public T newProxy(Class<T> tClass) {
        return proxyAgent.newProxy(tClass);
    }

    /**
     * 创建代理
     * @param <T>
     * @return
     */
    public static <T>T newCglibProxy(final Class<T> tClass, final MethodIntercept methodIntercept) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(tClass);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return methodIntercept.invoke(proxy, method, args, null);
            }
        });
        return (T) enhancer.create();
    }
    /**
     * 创建代理
     * @param <T>
     * @return
     */
    public static <T>T newJdkProxy(final Class<T> tClass, final MethodIntercept methodIntercept) {
        if(null == tClass || !tClass.isInterface()) {
            throw new NotSupportedException();
        }
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[]{tClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return methodIntercept.invoke(proxy, method, args, null);
            }
        });
    }
}


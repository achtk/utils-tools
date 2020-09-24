package com.chua.utils.tools.proxy;

import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.mapper.ProxyMapper;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib 代理
 * @author CH
 */
public class CglibProxyAgent<T> extends ProxyAgent<T> {

    public CglibProxyAgent(Class<T> source) {
        super(source);
    }

    public CglibProxyAgent(Class source, ProxyMapper proxyMapper) {
        super(source, proxyMapper);
    }

    public CglibProxyAgent(Class<T> source, MethodIntercept methodIntercept) {
        super(source, methodIntercept);
    }

    @Override
    public T newProxy() {
        Class<T> source = getSource();
        Enhancer enhancer = new Enhancer();
        if(source.isInterface()) {
            enhancer.setInterfaces(new Class[] {source});
        } else {
            enhancer.setSuperclass(source);
        }
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                ProxyMapper proxyMapper = getProxyMapper();
                return null == proxyMapper ? ProxyMapper.intercept(obj, method, args, proxy) : invoker(proxyMapper, obj, method, args, proxy);
            }
        });
        return (T) enhancer.create();
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

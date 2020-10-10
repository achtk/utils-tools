package com.chua.utils.tools.proxy;

import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.loader.BalancerLoader;
import com.chua.utils.tools.loader.RotationBalancerLoader;
import com.chua.utils.tools.mapper.AllProxyMapper;
import com.chua.utils.tools.mapper.ProxyMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * cglib 代理
 * @author CH
 */
public class CglibProxyAgent<T> implements ProxyAgent<T> {

    private ProxyMapper proxyMapper;
    private BalancerLoader balancerLoader = new RotationBalancerLoader();

    public CglibProxyAgent() {}
    public CglibProxyAgent(MethodIntercept methodIntercept) {
        this.proxyMapper = new AllProxyMapper(methodIntercept);
    }

    public CglibProxyAgent(ProxyMapper proxyMapper, BalancerLoader balancerLoader) {
        this.proxyMapper = proxyMapper;
        this.balancerLoader = balancerLoader;
    }

    @Override
    public T newProxy(Class<T> source) {
        Enhancer enhancer = new Enhancer();
        if(source.isInterface()) {
            enhancer.setInterfaces(new Class[] {source});
        } else {
            enhancer.setSuperclass(source);
        }
        enhancer.setCallback(new MethodInterceptorFactory());
        return (T) enhancer.create();
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

    private class MethodInterceptorFactory implements MethodInterceptor {
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return invoker(proxyMapper, obj, method, args, proxy);
        }
    }
}

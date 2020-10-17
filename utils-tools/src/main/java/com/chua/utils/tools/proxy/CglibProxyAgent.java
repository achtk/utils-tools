package com.chua.utils.tools.proxy;

import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.chua.utils.tools.loader.BalancerLoader;
import com.chua.utils.tools.loader.RotationBalancerLoader;
import com.chua.utils.tools.mapper.AllProxyMapper;
import com.chua.utils.tools.mapper.ProxyMapper;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib 代理
 * @author CH
 */
public class CglibProxyAgent<T> implements ProxyAgent<T>, MethodInterceptor {

    private ProxyMapper mapper;
    private BalancerLoader balancerLoader = new RotationBalancerLoader();

    public CglibProxyAgent() {}
    public CglibProxyAgent(MethodIntercept methodIntercept) {
        this.mapper = new AllProxyMapper(methodIntercept);
    }

    public CglibProxyAgent(ProxyMapper proxyMapper, BalancerLoader balancerLoader) {
        this.mapper = proxyMapper;
        this.balancerLoader = balancerLoader;
    }

    @Override
    public T newProxy(Class<T> source) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(source);
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    @Override
    public Object invoker(Object obj, Method method, Object[] args, Object proxy) throws Throwable {
        String methodName = method.getName();
        MethodProxy methodProxy = (MethodProxy) proxy;
        if(null == mapper || !mapper.hasName(methodName)) {
            return methodProxy.invokeSuper(obj, args);
        }

        MethodIntercept methodIntercept = mapper.tryToGetProxy(methodName, balancerLoader);
        if(null != methodIntercept) {
            return methodIntercept.invoke(obj, method, args, proxy);
        }
        return methodProxy.invokeSuper(obj, args);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return this.invoker(obj, method, args, proxy);
    }

}

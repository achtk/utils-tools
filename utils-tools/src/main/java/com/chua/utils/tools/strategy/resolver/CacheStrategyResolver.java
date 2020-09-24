package com.chua.utils.tools.strategy.resolver;

import com.chua.utils.tools.action.ActionListener;
import com.chua.utils.tools.cache.GuavaCacheProvider;
import com.chua.utils.tools.cache.ICacheProvider;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.config.StrategyProperties;
import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.proxy.ProxyAgent;
import com.google.common.base.Joiner;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 代理策略
 *
 * @author CH
 */
public class CacheStrategyResolver<T> implements IStrategyResolver<T> {

    private ICacheProvider<String, Object> provider = new GuavaCacheProvider<>();

    @Override
    public T callee(StrategyProperties strategyProperties) {
        ProxyAgent proxyAgent = getProxy(strategyProperties);
        return (T) proxyAgent.newProxy();
    }

    /**
     *
     * @param strategyProperties
     * @return
     */
    @Override
    public MethodIntercept methodIntercept(StrategyProperties strategyProperties) {
        ActionListener throwableListener = strategyProperties.getThrowableListener();
        Map<String, MethodIntercept> methodInterceptMap = strategyProperties.getMethodInterceptMap();
        return new MethodIntercept() {
            @Override
            public Object invoke(Object obj, Method method, Object[] args, Object... proxy) throws Throwable{
                String methodName = method.getName();
                if(MapHelper.containerKey(methodInterceptMap, methodName)) {
                    MethodIntercept methodIntercept = methodInterceptMap.get(methodName);
                    return methodIntercept.invoke(obj, method, args, proxy);
                }
                String className = obj.getClass().getName();
                String join = Joiner.on("_").join(className, methodName, args);
                if (!provider.container(join)) {
                    Object o1 = null;
                    try {
                        MethodProxy methodProxy = (MethodProxy) FinderHelper.firstElement(proxy);
                        o1 =  methodProxy.invokeSuper(obj, args);
                    } catch (Throwable throwable) {
                        if (null != throwableListener) {
                            throwableListener.listener(throwable);
                        }
                        o1 = strategyProperties.getDegrade();
                    }
                    provider.put(join, o1);
                }

                return (T) provider.get(join);
            }
        };
    }
}

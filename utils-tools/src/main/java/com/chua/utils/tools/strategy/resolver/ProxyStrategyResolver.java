package com.chua.utils.tools.strategy.resolver;

import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.config.StrategyProperties;
import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.handler.AgencyHandler;
import com.chua.utils.tools.proxy.ProxyAgent;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 代理策略
 * @author CH
 */
public class ProxyStrategyResolver<T> implements IStrategyResolver<T> {

    @Override
    public T callee(StrategyProperties strategyProperties) {
        ProxyAgent proxyAgent = getProxy(strategyProperties);
        return (T) proxyAgent.newProxy();
    }

    /**
     *
     * @return
     */
    @Override
    public MethodIntercept methodIntercept(StrategyProperties strategyProperties) {
        AgencyHandler agencyHandler = strategyProperties.getAgencyHandler();
        Map<String, MethodIntercept> methodInterceptMap = strategyProperties.getMethodInterceptMap();

        return new MethodIntercept() {

            @Override
            public Object invoke(Object obj, Method method, Object[] args, Object... proxy) throws Throwable {
                String methodName = method.getName();
                if(MapHelper.containerKey(methodInterceptMap, methodName)) {
                    MethodIntercept methodIntercept = methodInterceptMap.get(methodName);
                    return methodIntercept.invoke(obj, method, args, proxy);
                }
                if(null == agencyHandler) {
                    MethodProxy methodProxy = (MethodProxy) FinderHelper.firstElement(proxy);
                    return methodProxy.invokeSuper(obj, args);
                }
                return agencyHandler.intercept(obj, method, args, proxy);
            }
        };
    }
}

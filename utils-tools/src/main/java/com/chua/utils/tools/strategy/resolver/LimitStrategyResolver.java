package com.chua.utils.tools.strategy.resolver;

import com.chua.utils.tools.action.ActionListener;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.config.StrategyProperties;
import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.limit.ILimiterProvider;
import com.chua.utils.tools.limit.TokenLimitProvider;
import com.chua.utils.tools.proxy.ProxyAgent;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 代理策略
 *
 * @author CH
 */
public class LimitStrategyResolver<T> implements IStrategyResolver<T> {

    private ILimiterProvider limiterProvider = new TokenLimitProvider();
    private static final String DEFAULT_NAME = "default";
    private String name = DEFAULT_NAME;

    @Override
    public T callee(StrategyProperties strategyProperties) {
        limiterProvider.newLimiter(name, strategyProperties.getRetry());

        ProxyAgent proxyAgent = getProxy(strategyProperties);
        return (T) proxyAgent.newProxy(strategyProperties.getClasses());
    }

    @Override
    public MethodIntercept methodIntercept(StrategyProperties strategyProperties) {
        ActionListener throwableListener = strategyProperties.getThrowableListener();
        int timeout = strategyProperties.getTimeout();
        Map<String, MethodIntercept> methodInterceptMap = strategyProperties.getMethodInterceptMap();

        return new MethodIntercept() {
            @Override
            public Object invoke(Object obj, Method method, Object[] args, Object... proxy) throws Throwable {
                String methodName = method.getName();
                if (limiterProvider.tryAcquire(LimitStrategyResolver.this.name, timeout, TimeUnit.MILLISECONDS)) {
                    try {
                        if(MapHelper.containerKey(methodInterceptMap, methodName)) {
                            MethodIntercept methodIntercept = methodInterceptMap.get(methodName);
                            return methodIntercept.invoke(obj, method, args, proxy);
                        }
                        MethodProxy methodProxy = (MethodProxy) FinderHelper.firstElement(proxy);
                        return methodProxy.invokeSuper(obj, args);
                    } catch (Throwable throwable) {
                        if (null != throwableListener) {
                            throwableListener.listener(throwable);
                        }
                        return strategyProperties.getDegrade();
                    }
                }
                return strategyProperties.getDegrade();
            }

        };
    }
}

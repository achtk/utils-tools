package com.chua.utils.tools.strategy.resolver;

import com.chua.utils.tools.action.ActionListener;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.common.ObjectHelper;
import com.chua.utils.tools.config.StrategyProperties;
import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.predicate.FalsePredicate;
import com.chua.utils.tools.proxy.ProxyAgent;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.google.common.base.Predicate;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 按次数重试
 * @author CH
 */
public class RetryConditionStrategyResolver implements IStrategyResolver {
    @Override
    public Object callee(StrategyProperties strategyProperties) {
        ProxyAgent proxyAgent = getProxy(strategyProperties);
        return proxyAgent.newProxy(strategyProperties.getClasses());
    }

    @Override
    public MethodIntercept methodIntercept(StrategyProperties strategyProperties) {
        ActionListener throwableListener = strategyProperties.getThrowableListener();
        Predicate predicate = ObjectHelper.defaultNoneNull(strategyProperties.getPredicate(), FalsePredicate.INSTANCE);
        Map<String, MethodIntercept> methodInterceptMap = strategyProperties.getMethodInterceptMap();

        Retryer retries = RetryerBuilder.newBuilder()
                //抛出runtime重试，error不重试
                .retryIfResult(predicate)
                .withStopStrategy(StopStrategies.stopAfterAttempt(strategyProperties.getRetry()))
                .build();
        return new MethodIntercept() {
            @Override
            public Object invoke(Object obj, Method method, Object[] args, Object... proxy) throws Throwable {
                return retries.call(new Callable<Object>() {
                    @Override
                    public Object call() {
                        try {
                            String methodName = method.getName();
                            if(MapHelper.containerKey(methodInterceptMap, methodName)) {
                                MethodIntercept methodIntercept = methodInterceptMap.get(methodName);
                                return methodIntercept.invoke(obj, method, args, proxy);
                            }
                            MethodProxy methodProxy = (MethodProxy) FinderHelper.firstElement(proxy);
                            return methodProxy.invokeSuper(obj, args);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            ObjectHelper.invoke(throwableListener, "listener", e);
                        }
                        return strategyProperties.getDegrade();
                    }
                });
            }
        };
    }
}

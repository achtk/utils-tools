package com.chua.utils.tools.strategy.resolver;

import com.chua.utils.tools.action.ActionListener;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.config.StrategyProperties;
import com.chua.utils.tools.function.MethodIntercept;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * 异步代理策略
 * @author CH
 */
public class AsyncStrategyResolver<T> implements IStrategyResolver<T>, Closeable {

    private final ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor();

    @Override
    public T callee(StrategyProperties strategyProperties) {
        return getProxy(strategyProperties).newProxy();
    }

    @Override
    public MethodIntercept methodIntercept(StrategyProperties strategyProperties) {
        ActionListener asyncListener = strategyProperties.getAsyncListener();
        ActionListener throwableListener = strategyProperties.getThrowableListener();
        Map<String, MethodIntercept> methodInterceptMap = strategyProperties.getMethodInterceptMap();

        return new MethodIntercept() {
            @Override
            public Object invoke(Object obj, Method method, Object[] args, Object... proxy) throws Throwable {
                String methodName = method.getName();
                CompletableFuture.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        Object o = null;
                        try {
                            if(MapHelper.containerKey(methodInterceptMap, methodName)) {
                                MethodIntercept methodIntercept = methodInterceptMap.get(methodName);
                                o = methodIntercept.invoke(obj, method, args, proxy);
                            }
                            MethodProxy methodProxy = (MethodProxy) FinderHelper.firstElement(proxy);
                            o =  methodProxy.invokeSuper(obj, args);
                            if(null != asyncListener) {
                                asyncListener.listener(o);
                            }
                        } catch (Throwable throwable) {
                            if(null != throwableListener) {
                                throwableListener.listener(throwable);
                            }
                            if(null != asyncListener) {
                                asyncListener.listener(strategyProperties.getDegrade());
                            }
                        }
                        return;
                    }
                }, executorService);

                return null;
            }

        };
    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
        executorService.shutdownNow();
    }
}

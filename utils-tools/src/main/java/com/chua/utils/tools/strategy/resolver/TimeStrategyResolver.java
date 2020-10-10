package com.chua.utils.tools.strategy.resolver;

import com.chua.utils.tools.action.ActionListener;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.config.StrategyProperties;
import com.chua.utils.tools.function.MethodIntercept;
import net.sf.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 超时策略
 * @author CH
 */
public class TimeStrategyResolver implements IStrategyResolver, AutoCloseable {

    private final ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor();

    @Override
    public Object callee(StrategyProperties strategyProperties) {
        return getProxy(strategyProperties).newProxy(strategyProperties.getClasses());
    }

    @Override
    public MethodIntercept methodIntercept(StrategyProperties strategyProperties) {
        int timeout = strategyProperties.getTimeout();
        ActionListener throwableListener = strategyProperties.getThrowableListener();
        Map<String, MethodIntercept> methodInterceptMap = strategyProperties.getMethodInterceptMap();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        return new MethodIntercept() {
            @Override
            public Object invoke(Object obj, Method method, Object[] args, Object... proxy) throws Throwable {
                Future submit = threadPoolExecutor.submit(new Callable() {

                    @Override
                    public Object call() throws Exception {
                        try {
                            String methodName = method.getName();
                            if(MapHelper.containerKey(methodInterceptMap, methodName)) {
                                MethodIntercept methodIntercept = methodInterceptMap.get(methodName);
                                return methodIntercept.invoke(obj, method, args, proxy);
                            }
                            MethodProxy methodProxy = (MethodProxy) FinderHelper.firstElement(proxy);
                            return methodProxy.invokeSuper(obj, args);
                        } catch (Throwable throwable) {
                            return null;
                        }
                    }
                });
                try {
                    return submit.get(timeout, TimeUnit.MILLISECONDS);
                } catch (Throwable throwable) {
                    if(null != throwableListener) {
                        throwableListener.listener(throwable);
                    }
                } finally {
                    threadPoolExecutor.shutdownNow();
                }

                return strategyProperties.getDegrade();
            }
        };
    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
        executorService.shutdownNow();
    }
}

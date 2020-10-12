package com.chua.utils.tools.function.intercept;

import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.mapper.ProxyMapper;
import com.chua.utils.tools.strategy.handler.IAsyncStrategyPolicy;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * 异步方法拦截器
 * @author CH
 * @version 1.0.0
 * @since 2020/10/12
 */
@AllArgsConstructor
public class ScheduleMethodIntercept implements MethodIntercept {

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
    private IAsyncStrategyPolicy asyncStrategyPolicy;
    private String[] exclude = ArraysHelper.emptyString();
    private long delay;
    @Override
    public Object invoke(Object obj, Method method, Object[] args, Object... proxy) throws Throwable {
        if(ArraysHelper.contains(exclude, method.getName())) {
            return ProxyMapper.intercept(obj, method, args, proxy);
        }
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Object intercept = ProxyMapper.intercept(obj, method, args, proxy);
                    asyncStrategyPolicy.result(intercept);
                } catch (Throwable throwable) {
                    asyncStrategyPolicy.degradeResult(throwable);
                }
            }
        }, 0, delay, TimeUnit.SECONDS);
        return null;
    }
}

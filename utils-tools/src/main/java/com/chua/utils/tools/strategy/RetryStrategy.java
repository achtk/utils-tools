package com.chua.utils.tools.strategy;

import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 重试策略
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
@AllArgsConstructor
@NoArgsConstructor
public class RetryStrategy<T> extends StandardProxyStrategy<T> implements Strategy<T>, MethodIntercept<MethodProxy> {

    @Setter
    private int retry = 3;

    /**
     * 重试时间
     */
    private long timeout = 3000;

    /**
     * 自增
     */
    private long increment = 0;
    /**
     * 规则
     */
    private Predicate predicate;

    public RetryStrategy(int retry) {
        this.retry = retry;
    }

    public RetryStrategy(Predicate predicate) {
        this.predicate = predicate;
    }


    @Override
    public Object invoke(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Retryer<Object> retries = RetryerBuilder.<T>newBuilder()
                //抛出runtime重试，error不重试
                .retryIfException(predicate)
                .retryIfResult(predicate)
                .withStopStrategy(StopStrategies.stopAfterAttempt(retry))
                .withWaitStrategy(WaitStrategies.incrementingWait(timeout, TimeUnit.MILLISECONDS, increment, TimeUnit.MILLISECONDS))
                .build();

        return retries.call(() -> {
            try {
                return method.invoke(getSource(), args);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public T create(T source) {
        super.setMethodIntercept(this);
        return super.proxy(source);
    }
}

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
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
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
    @Setter
    private long timeout = 3000;

    /**
     * 自增
     */
    private long increment = 0;
    /**
     * 异常规则
     */
    @Setter
    private Predicate throwPredicate;
    /**
     * 返回子规则
     */
    @Setter
    private Predicate returnPredicate;

    public RetryStrategy(Predicate predicate) {
        this.throwPredicate = predicate;
    }

    public RetryStrategy(int retry) {
        this.retry = retry;
    }


    @Override
    public Object invoke(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        RetryerBuilder<T> retryerBuilder = RetryerBuilder.<T>newBuilder();
        retryerBuilder.withStopStrategy(StopStrategies.stopAfterAttempt(retry));
        retryerBuilder.withWaitStrategy(WaitStrategies.incrementingWait(timeout, TimeUnit.MILLISECONDS, increment, TimeUnit.MILLISECONDS));

        if (throwPredicate != null) {
            retryerBuilder.retryIfException(throwPredicate);
        }

        if (throwPredicate != null) {
            retryerBuilder.retryIfResult(returnPredicate);
        }
        Retryer<T> retries = retryerBuilder.build();

        return retries.call(() -> {
            try {
                return (T) method.invoke(getSource(), args);
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

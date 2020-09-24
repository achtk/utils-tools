package com.chua.utils.tools.strategy.helper;

import com.chua.utils.tools.strategy.handler.IStrategyPolicy;
import com.github.rholder.retry.*;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**重试机制
 * @author CH
 */
public class RetryStrategy {

    /**
     *
     * 无限重试直到没有异常
     * @param retryPolicy 重试策略
     * @param <T>
     * @return
     */
    public static <T>T retryWithException(final IStrategyPolicy<T> retryPolicy) {

        Preconditions.checkArgument(null != retryPolicy);

        Retryer<T> retries = RetryerBuilder.<T>newBuilder()
                //抛出runtime重试，error不重试
                .retryIfRuntimeException()
                .withStopStrategy(StopStrategies.neverStop())
                .build();

        return call(retries, retryPolicy);
    }

    /**
     *
     * 最大尝试次数策略
     * @param retryPolicy 重试策略
     * @param limit 最大尝试次数
     * @param sleepTime 间隔
     * @param <T>
     * @return
     */
    public static <T>T retryWithLimit(final IStrategyPolicy<T> retryPolicy, final int limit, final long sleepTime) {
        return retryWithLimit(retryPolicy, limit, sleepTime, null);
    }
    /**
     *
     * 最大尝试次数策略
     * @param retryPolicy 重试策略
     * @param limit 最大尝试次数
     * @param sleepTime 间隔
     * @param <T>
     * @return
     */
    public static <T>T retryWithLimit(final IStrategyPolicy<T> retryPolicy, final int limit, final long sleepTime, final String retryCondition) {

        Preconditions.checkArgument(null != retryPolicy);
        Preconditions.checkArgument(limit > 0);
        Preconditions.checkArgument(sleepTime > 0);

        RetryerBuilder<T> retries = RetryerBuilder.<T>newBuilder()
                .retryIfException()
                .withStopStrategy(StopStrategies.stopAfterAttempt(limit))
                .withWaitStrategy(WaitStrategies.fixedWait(sleepTime, TimeUnit.SECONDS));

        if(!Strings.isNullOrEmpty(retryCondition)) {
            Predicate<T> charSequencePredicate = (Predicate<T>) Predicates.containsPattern(retryCondition);
            retries.retryIfResult(charSequencePredicate);
        }
        Retryer<T> build = retries.build();


       return call(build, retryPolicy);
    }

    /**
     * 重试工厂
     * @param retryPolicy 方法
     * @param tReorderBuilder 重试构造器
     * @param <T>
     * @return
     */
    public static <T>T retryWithFactory(final IStrategyPolicy<T> retryPolicy, final RetryerBuilder<T> tReorderBuilder) {
        Preconditions.checkArgument(null != tReorderBuilder);
        Retryer<T> build = tReorderBuilder.build();
        return call(build, retryPolicy);
    }
    /**
     *
     * 间隔尝试次数策略
     * @param retryPolicy 重试策略
     * @param sleepTime 间隔
     * @param <T>
     * @return
     */
    public static <T>T retryWithSleepTime(final IStrategyPolicy<T> retryPolicy, final long sleepTime, final Integer... limit) {

        Preconditions.checkArgument(null != retryPolicy);
        Preconditions.checkArgument(sleepTime > 0);

        RetryerBuilder<T> tReorderBuilder = RetryerBuilder.<T>newBuilder()
                .retryIfRuntimeException()
                .withWaitStrategy(WaitStrategies.fixedWait(sleepTime, TimeUnit.SECONDS));


        setMaxLimit(tReorderBuilder, limit);

        Retryer<T> retries = tReorderBuilder.build();
        return call(retries, retryPolicy);
    }
    /**
     *
     * 间隔递增尝试次数策略
     * @param retryPolicy 重试策略
     * @param sleepTime 间隔
     * @param increaseTime 每次尝试递增间隔
     * @param <T>
     * @return
     */
    public static <T>T retryWithIncreaseSleepTime(final IStrategyPolicy<T> retryPolicy, final long sleepTime, final long increaseTime, final Integer...limit) {

        Preconditions.checkArgument(null != retryPolicy);
        Preconditions.checkArgument(sleepTime > 0);

        RetryerBuilder<T> tReorderBuilder = RetryerBuilder.<T>newBuilder()
                .retryIfRuntimeException()
                .withWaitStrategy(WaitStrategies.incrementingWait(sleepTime, TimeUnit.SECONDS, increaseTime, TimeUnit.MILLISECONDS));

        setMaxLimit(tReorderBuilder, limit);

        Retryer<T> retries = tReorderBuilder.build();
        return call(retries, retryPolicy);
    }

    /**
     * 设置最大尝试次数
     */
    private static <T>void setMaxLimit(RetryerBuilder<T> tRetryerBuilder, final Integer...limit) {
        if(null != limit && limit.length > 0) {
            int limit1 = limit[0];
            if(limit1 > 0) {
                tRetryerBuilder.withStopStrategy(StopStrategies.stopAfterAttempt(limit1));
            } else {
                tRetryerBuilder.withStopStrategy(StopStrategies.neverStop());
            }
        } else {
            tRetryerBuilder.withStopStrategy(StopStrategies.neverStop());
        }

    }

    /**
     * 返回值
     * @return
     */
    private static <T>T call(final Retryer<T> retries, final IStrategyPolicy<T> retryPolicy) {
        try {
            return retries.call(new Callable<T>() {
                @Override
                public T call() {
                    return retryPolicy.policy();
                }
            });
        } catch (ExecutionException e) {
            return retryPolicy.failure(e);
        } catch (RetryException e) {
            return retryPolicy.failure(e);
        }
    }


}

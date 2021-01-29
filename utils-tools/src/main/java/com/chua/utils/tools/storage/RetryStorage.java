package com.chua.utils.tools.storage;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicate;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 重试
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class RetryStorage {
    private static final int RETRY = 3;
    private static final TimeUnit UNIT = TimeUnit.MILLISECONDS;
    private static final long TIMEOUT = 0_000L;

    /**
     * 执行结果{固定等待时长策略}重试
     *
     * @param supplier  回调
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result run(Supplier<Result> supplier, Predicate<Throwable> predicate) {
        return run(supplier, RETRY, 0, predicate);
    }

    /**
     * 执行结果{固定等待时长策略}重试
     *
     * @param supplier  回调
     * @param retry     尝试次数
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result run(Supplier<Result> supplier, int retry, Predicate<Throwable> predicate) {
        return run(supplier, retry, TIMEOUT, predicate);
    }

    /**
     * 执行结果{固定等待时长策略}重试
     *
     * @param supplier  回调
     * @param retry     尝试次数
     * @param exception 异常
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result run(Supplier<Result> supplier, int retry, Class<Throwable> exception) {
        return run(supplier, retry, TIMEOUT, new Predicate<Throwable>() {
            @Override
            public boolean apply(@Nullable Throwable throwable) {
                return exception.equals(throwable);
            }
        });
    }

    /**
     * 执行异常{固定等待时长策略}重试
     *
     * @param supplier  回调
     * @param retry     尝试次数
     * @param timeout   超时时间
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result run(Supplier<Result> supplier, int retry, long timeout, Predicate<Throwable> predicate) {
        RetryerBuilder<Result> builder = RetryerBuilder.newBuilder();
        builder.withStopStrategy(StopStrategies.stopAfterAttempt(retry));
        builder.withWaitStrategy(WaitStrategies.fixedWait(timeout, UNIT));

        if (predicate != null) {
            builder.retryIfException(predicate);
        }
        return build(supplier, builder);
    }

    /**
     * 执行结果{固定等待时长策略}重试
     *
     * @param supplier  回调
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result doWithFixedWaitResult(Supplier<Result> supplier, Predicate<Result> predicate) {
        return doWithFixedWaitResult(supplier, RETRY, TIMEOUT, predicate);
    }

    /**
     * 执行结果{固定等待时长策略}重试
     *
     * @param supplier  回调
     * @param retry     尝试次数
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result doWithFixedWaitResult(Supplier<Result> supplier, int retry, Predicate<Result> predicate) {
        return doWithFixedWaitResult(supplier, retry, TIMEOUT, predicate);
    }

    /**
     * 执行结果{固定等待时长策略}重试
     *
     * @param supplier  回调
     * @param retry     尝试次数
     * @param timeout   超时时间
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result doWithFixedWaitResult(Supplier<Result> supplier, int retry, long timeout, Predicate<Result> predicate) {
        RetryerBuilder<Result> builder = RetryerBuilder.newBuilder();
        builder.withStopStrategy(StopStrategies.stopAfterAttempt(retry));
        builder.withWaitStrategy(WaitStrategies.fixedWait(timeout, UNIT));

        if (predicate != null) {
            builder.retryIfResult(predicate);
        }
        return build(supplier, builder);
    }

    /**
     * 执行结果{递增等待时长策略(提供一个初始值和步长，等待时间随重试次数增加而增加)}重试
     *
     * @param supplier  回调
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result doWithIncrementingWaitException(Supplier<Result> supplier, Predicate<Throwable> predicate) {
        return doWithIncrementingWaitException(supplier, RETRY, 0, TIMEOUT, predicate);
    }

    /**
     * 执行结果{递增等待时长策略(提供一个初始值和步长，等待时间随重试次数增加而增加)}重试
     *
     * @param supplier  回调
     * @param retry     尝试次数
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result doWithIncrementingWaitException(Supplier<Result> supplier, int retry, Predicate<Throwable> predicate) {
        return doWithIncrementingWaitException(supplier, retry, retry, 0, predicate);
    }

    /**
     * 执行结果{递增等待时长策略(提供一个初始值和步长，等待时间随重试次数增加而增加)}重试
     *
     * @param supplier  回调
     * @param retry     尝试次数
     * @param timeout   超时时间
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result doWithIncrementingWaitException(Supplier<Result> supplier, int retry, int timeout, Predicate<Throwable> predicate) {
        return doWithIncrementingWaitException(supplier, retry, retry, timeout, predicate);
    }

    /**
     * 执行异常{递增等待时长策略(提供一个初始值和步长，等待时间随重试次数增加而增加)}重试
     *
     * @param supplier  回调
     * @param retry     尝试次数
     * @param timeout   超时时间
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result doWithIncrementingWaitException(Supplier<Result> supplier, int retry, int increment, long timeout, Predicate<Throwable> predicate) {
        RetryerBuilder<Result> builder = RetryerBuilder.newBuilder();
        builder.withStopStrategy(StopStrategies.stopAfterAttempt(retry));
        builder.withWaitStrategy(WaitStrategies.incrementingWait(timeout, UNIT, increment, UNIT));

        if (predicate != null) {
            builder.retryIfException(predicate);
        }
        return build(supplier, builder);
    }

    /**
     * 执行结果{递增等待时长策略(提供一个初始值和步长，等待时间随重试次数增加而增加)}重试
     *
     * @param supplier  回调
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result doWithIncrementingWaitResult(Supplier<Result> supplier, Predicate<Result> predicate) {
        return doWithIncrementingWaitResult(supplier, RETRY, 0, TIMEOUT, predicate);
    }

    /**
     * 执行结果{递增等待时长策略(提供一个初始值和步长，等待时间随重试次数增加而增加)}重试
     *
     * @param supplier  回调
     * @param retry     尝试次数
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result doWithIncrementingWaitResult(Supplier<Result> supplier, int retry, Predicate<Result> predicate) {
        return doWithIncrementingWaitResult(supplier, retry, retry, TIMEOUT, predicate);
    }

    /**
     * 执行结果{递增等待时长策略(提供一个初始值和步长，等待时间随重试次数增加而增加)}重试
     *
     * @param supplier  回调
     * @param retry     尝试次数
     * @param timeout   超时时间
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result doWithIncrementingWaitResult(Supplier<Result> supplier, int retry, int timeout, Predicate<Result> predicate) {
        return doWithIncrementingWaitResult(supplier, retry, retry, timeout, predicate);
    }

    /**
     * 执行结果{随机等待时长策略(可以提供一个最小和最大时长，等待时长为其区间随机值)}重试
     *
     * @param supplier  回调
     * @param retry     尝试次数
     * @param increment 增长
     * @param timeout   超时时间
     * @param predicate 条件
     * @param <Result>  结果类型
     * @return 结果
     */
    public static <Result> Result doWithIncrementingWaitResult(Supplier<Result> supplier, int retry, int increment, long timeout, Predicate<Result> predicate) {
        RetryerBuilder<Result> builder = RetryerBuilder.newBuilder();
        builder.withStopStrategy(StopStrategies.stopAfterAttempt(retry));
        builder.withWaitStrategy(WaitStrategies.incrementingWait(timeout, UNIT, increment, UNIT));

        if (predicate != null) {
            builder.retryIfResult(predicate);
        }
        return build(supplier, builder);
    }


    /**
     * 执行结果{随机等待时长策略(可以提供一个最小和最大时长，等待时长为其区间随机值)}重试
     *
     * @param supplier    回调
     * @param minimumTime 最小时长
     * @param maximumTime 最大时长
     * @param predicate   条件
     * @param <Result>    结果类型
     * @return 结果
     */
    public static <Result> Result doWithRandomWaitException(Supplier<Result> supplier, long minimumTime, long maximumTime, Predicate<Throwable> predicate) {
        return doWithRandomWaitException(supplier, RETRY, minimumTime, maximumTime, predicate);
    }

    /**
     * 执行结果{随机等待时长策略(可以提供一个最小和最大时长，等待时长为其区间随机值)}重试
     *
     * @param supplier    回调
     * @param retry       尝试次数
     * @param minimumTime 最小时长
     * @param maximumTime 最大时长
     * @param predicate   条件
     * @param <Result>    结果类型
     * @return 结果
     */
    public static <Result> Result doWithRandomWaitException(Supplier<Result> supplier, int retry, long minimumTime, long maximumTime, Predicate<Throwable> predicate) {
        RetryerBuilder<Result> builder = RetryerBuilder.newBuilder();
        builder.withStopStrategy(StopStrategies.stopAfterAttempt(retry));
        builder.withWaitStrategy(WaitStrategies.randomWait(minimumTime, UNIT, maximumTime, UNIT));

        if (predicate != null) {
            builder.retryIfException(predicate);
        }
        return build(supplier, builder);
    }

    /**
     * 执行结果{随机等待时长策略(可以提供一个最小和最大时长，等待时长为其区间随机值)}重试
     *
     * @param supplier    回调
     * @param minimumTime 最小时长
     * @param maximumTime 最大时长
     * @param predicate   条件
     * @param <Result>    结果类型
     * @return 结果
     */
    public static <Result> Result doWithRandomWaitResult(Supplier<Result> supplier, long minimumTime, long maximumTime, Predicate<Result> predicate) {
        return doWithRandomWaitResult(supplier, RETRY, minimumTime, maximumTime, predicate);
    }

    /**
     * 执行结果{随机等待时长策略(可以提供一个最小和最大时长，等待时长为其区间随机值)}重试
     *
     * @param supplier    回调
     * @param retry       尝试次数
     * @param minimumTime 最小时长
     * @param maximumTime 最大时长
     * @param predicate   条件
     * @param <Result>    结果类型
     * @return 结果
     */
    public static <Result> Result doWithRandomWaitResult(Supplier<Result> supplier, int retry, long minimumTime, long maximumTime, Predicate<Result> predicate) {
        RetryerBuilder<Result> builder = RetryerBuilder.newBuilder();
        builder.withStopStrategy(StopStrategies.stopAfterAttempt(retry));
        builder.withWaitStrategy(WaitStrategies.randomWait(minimumTime, UNIT, maximumTime, UNIT));

        if (predicate != null) {
            builder.retryIfResult(predicate);
        }
        return build(supplier, builder);
    }


    /**
     * 执行结果{指数等待时长策略}重试
     *
     * @param supplier    回调
     * @param maximumTime 最大等待时间
     * @param predicate   条件
     * @param <Result>    结果类型
     * @return 结果
     */
    public static <Result> Result doWithExponentialWaitResult(Supplier<Result> supplier, long maximumTime, Predicate<Result> predicate) {
        return doWithExponentialWaitResult(supplier, RETRY, maximumTime, predicate);
    }

    /**
     * 执行结果{指数等待时长策略}重试
     *
     * @param supplier    回调
     * @param retry       尝试次数
     * @param maximumTime 最大等待时间
     * @param predicate   条件
     * @param <Result>    结果类型
     * @return 结果
     */
    public static <Result> Result doWithExponentialWaitResult(Supplier<Result> supplier, int retry, long maximumTime, Predicate<Result> predicate) {
        RetryerBuilder<Result> builder = RetryerBuilder.newBuilder();
        builder.withStopStrategy(StopStrategies.stopAfterAttempt(retry));
        builder.withWaitStrategy(WaitStrategies.exponentialWait(maximumTime, UNIT));

        if (predicate != null) {
            builder.retryIfResult(predicate);
        }
        return build(supplier, builder);
    }

    /**
     * 执行结果{指数等待时长策略}重试
     *
     * @param supplier    回调
     * @param maximumTime 最大等待时间
     * @param predicate   条件
     * @param <Result>    结果类型
     * @return 结果
     */
    public static <Result> Result doWithExponentialWaitException(Supplier<Result> supplier, long maximumTime, Predicate<Throwable> predicate) {
        return doWithExponentialWaitException(supplier, RETRY, maximumTime, predicate);
    }

    /**
     * 执行结果{指数等待时长策略}重试
     *
     * @param supplier    回调
     * @param retry       尝试次数
     * @param maximumTime 最大等待时间
     * @param predicate   条件
     * @param <Result>    结果类型
     * @return 结果
     */
    public static <Result> Result doWithExponentialWaitException(Supplier<Result> supplier, int retry, long maximumTime, Predicate<Throwable> predicate) {
        RetryerBuilder<Result> builder = RetryerBuilder.newBuilder();
        builder.withStopStrategy(StopStrategies.stopAfterAttempt(retry));
        builder.withWaitStrategy(WaitStrategies.exponentialWait(maximumTime, UNIT));

        if (predicate != null) {
            builder.retryIfException(predicate);
        }
        return build(supplier, builder);
    }

    /**
     * 构建
     *
     * @param supplier 方法回调
     * @param builder  构建器
     * @param <Result> 结果类型
     * @return 结果
     */
    private static <Result> Result build(Supplier<Result> supplier, RetryerBuilder<Result> builder) {
        Retryer<Result> retries = builder.build();
        try {
            return retries.call(() -> {
                try {
                    return supplier.get();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

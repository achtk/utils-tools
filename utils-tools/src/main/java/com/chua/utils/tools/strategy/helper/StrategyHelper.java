package com.chua.utils.tools.strategy.helper;

import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.function.intercept.*;
import com.chua.utils.tools.proxy.CglibProxyAgent;
import com.chua.utils.tools.proxy.DefaultProxyAgent;
import com.chua.utils.tools.proxy.ProxyAgent;
import com.chua.utils.tools.proxy.ProxyLoader;
import com.chua.utils.tools.strategy.handler.IAsyncStrategyPolicy;
import com.chua.utils.tools.strategy.handler.IStrategyPolicy;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 策略工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/12
 */
public class StrategyHelper {

    private static final String DEFAULT_GROUP = "DEFAULT";

    /**
     * 创建策略
     *
     * @param tClass 类型
     * @param <T>
     * @return
     */
    public static <T> T doWithIntercept(final MethodIntercept methodIntercept, final Class<T> tClass) {
        ProxyAgent<T> proxyAgent = new CglibProxyAgent<>(methodIntercept);
        ProxyLoader<T> proxyLoader = new ProxyLoader<>(proxyAgent);
        return proxyLoader.newProxy(tClass);
    }

    /**
     * 创建代理策略
     *
     * @param strategyPolicy 策略
     * @param tClass         类型
     * @param <T>
     * @return
     */
    public static <T> T doWithProxy(final IStrategyPolicy<MethodIntercept> strategyPolicy, final Class<T> tClass) {
        return doWithIntercept(new MethodIntercept() {
            @Override
            public Object invoke(Object obj, Method method, Object[] args, Object proxy) throws Throwable {
                MethodIntercept policy = strategyPolicy.policy();
                return policy.invoke(obj, method, args, proxy);
            }
        }, tClass);
    }

    /**
     * 创建代理策略
     *
     * @param strategyPolicy 策略
     * @param tClass         类型
     * @param <T>
     * @return
     */
    public static <T> T doWithInterfaceProxy(final IStrategyPolicy<MethodIntercept> strategyPolicy, final Class<T> tClass) {
        ProxyAgent<T> proxyAgent = new DefaultProxyAgent(new MethodIntercept() {
            @Override
            public Object invoke(Object obj, Method method, Object[] args, Object proxy) throws Throwable {
                MethodIntercept policy = strategyPolicy.policy();
                return policy.invoke(obj, method, args, proxy);
            }
        });
        ProxyLoader<T> proxyLoader = new ProxyLoader<>(proxyAgent);
        return proxyLoader.newProxy(tClass);
    }

    /**
     * 调度策略
     *
     * @param tClass         类
     * @param strategyPolicy 消费者
     * @param <T>
     * @return 代理对象
     */
    public static <T> T doWithSchedule(final IAsyncStrategyPolicy<MethodIntercept> strategyPolicy, final Class<T> tClass, final long delay, final String... exclude) {
        return doWithProxy(new IStrategyPolicy<MethodIntercept>() {
            @Override
            public MethodIntercept policy() {
                return new ScheduleMethodIntercept(strategyPolicy, exclude, delay);
            }

            @Override
            public MethodIntercept failure(Throwable throwable) {
                return null;
            }
        }, tClass);
    }

    /**
     * 异步策略
     *
     * @param tClass         类
     * @param strategyPolicy 消费者
     * @param <T>
     * @return 代理对象
     */
    public static <T> T doWithAsync(final IAsyncStrategyPolicy<MethodIntercept> strategyPolicy, final Class<T> tClass, final String... exclude) {
        return doWithProxy(new IStrategyPolicy<MethodIntercept>() {
            @Override
            public MethodIntercept policy() {
                return new AsyncMethodIntercept(strategyPolicy, exclude);
            }

            @Override
            public MethodIntercept failure(Throwable throwable) {
                return null;
            }
        }, tClass);
    }

    /**
     * 执行并缓存数据
     *
     * @param cacheKey 缓存Key
     * @param supplier 执行方法
     * @param <T>
     * @return
     */
    public static <K, T> T doWithCache(final K cacheKey, final Supplier<T> supplier, CacheProvider<K, T> cacheProvider) {
        if (null == cacheProvider) {
            cacheProvider = new ConcurrentCacheProvider<>();
        }
        if (null != cacheKey && cacheProvider.containsKey(cacheKey)) {
            return cacheProvider.get(cacheKey);
        }
        T t = supplier.get();
        if (null != cacheKey) {
            cacheProvider.put(cacheKey, t);
        }
        return t;
    }

    /**
     * 缓存策略
     *
     * @param tClass          类
     * @param methodIntercept 方法拦截器用于接口
     * @param <T>
     * @return 代理对象
     */
    public static <T> T doWithCache(final Class<T> tClass, final MethodIntercept methodIntercept, final String... exclude) {
        if (tClass.isInterface()) {
            return doWithInterfaceProxy(new IStrategyPolicy<MethodIntercept>() {
                @Override
                public MethodIntercept policy() {
                    return methodIntercept;
                }

                @Override
                public MethodIntercept failure(Throwable throwable) {
                    return methodIntercept;
                }
            }, tClass);
        }
        return doWithProxy(new IStrategyPolicy<MethodIntercept>() {
            @Override
            public MethodIntercept policy() {
                return new CacheMethodIntercept(exclude);
            }

            @Override
            public MethodIntercept failure(Throwable throwable) {
                return null;
            }
        }, tClass);
    }

    /**
     * 限流策略
     *
     * @param tClass          类
     * @param methodIntercept 方法拦截器用于接口
     * @param size            令牌数量
     * @param <T>
     * @return 代理对象
     */
    public static <T> T doWithLimit(final Class<T> tClass, final MethodIntercept methodIntercept, final int size, final String... exclude) {
        return doWithLimit(tClass, methodIntercept, DEFAULT_GROUP, size);
    }

    /**
     * 限流策略
     *
     * @param tClass          类
     * @param methodIntercept 方法拦截器用于接口
     * @param size            令牌数量
     * @param group           分组
     * @param <T>
     * @return 代理对象
     */
    public static <T> T doWithLimit(final Class<T> tClass, final MethodIntercept methodIntercept, final String group, final int size, final String... exclude) {
        if (tClass.isInterface()) {
            return doWithInterfaceProxy(new IStrategyPolicy<MethodIntercept>() {
                @Override
                public MethodIntercept policy() {
                    return methodIntercept;
                }

                @Override
                public MethodIntercept failure(Throwable throwable) {
                    return methodIntercept;
                }
            }, tClass);
        }
        return doWithProxy(new IStrategyPolicy<MethodIntercept>() {
            @Override
            public MethodIntercept policy() {
                return new LimitMethodIntercept(group, size, exclude);
            }

            @Override
            public MethodIntercept failure(Throwable throwable) {
                return null;
            }
        }, tClass);
    }

    /**
     * 重试策略
     *
     * @param strategyPolicy 策略
     * @param <T>
     * @return 数据结果
     */
    public static <T> T doWithRetry(IStrategyPolicy<T> strategyPolicy) {
        Preconditions.checkArgument(null != strategyPolicy);
        Retryer<T> retries = RetryerBuilder.<T>newBuilder()
                //抛出runtime重试，error不重试
                .retryIfException()
                .withStopStrategy(StopStrategies.neverStop())
                .build();
        return RetryStrategy.call(retries, strategyPolicy);
    }

    /**
     * 重试策略
     *
     * @param strategyPolicy 策略
     * @param exceptionClass 异常类型
     * @param <T>
     * @return 数据结果
     */
    public static <T> T doWithRetry(IStrategyPolicy<T> strategyPolicy, final @Nonnull Class<? extends Throwable> exceptionClass) {
        Preconditions.checkArgument(null != strategyPolicy);
        Retryer<T> retries = RetryerBuilder.<T>newBuilder()
                //抛出runtime重试，error不重试
                .retryIfExceptionOfType(exceptionClass)
                .withStopStrategy(StopStrategies.neverStop())
                .build();
        return RetryStrategy.call(retries, strategyPolicy);
    }

    /**
     * 重试策略
     *
     * @param strategyPolicy 策略
     * @param predicate      异常类型
     * @param <T>
     * @return 数据结果
     */
    public static <T> T doWithRetry(IStrategyPolicy<T> strategyPolicy, @Nonnull Predicate<T> predicate) {
        Preconditions.checkArgument(null != strategyPolicy);
        Retryer<T> retries = RetryerBuilder.<T>newBuilder()
                //抛出runtime重试，error不重试
                .retryIfResult(predicate)
                .withStopStrategy(StopStrategies.neverStop())
                .build();
        return RetryStrategy.call(retries, strategyPolicy);
    }

    /**
     * 超时策略
     *
     * @param strategyPolicy 策略
     * @param timoutMs       超时策略
     * @param <T>
     * @return 数据结果
     */
    public static <T> T doWithTimeout(IStrategyPolicy<T> strategyPolicy, final long timoutMs) {
        Preconditions.checkArgument(timoutMs > 0, "time should be bigger than 0");
        Preconditions.checkArgument(null != strategyPolicy, "strategyPolicy should not be null");

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        Future<T> submit = threadPoolExecutor.submit(new Callable<T>() {

            @Override
            public T call() throws Exception {
                return strategyPolicy.policy();
            }
        });
        try {
            return submit.get(timoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return strategyPolicy.failure(e);
        } catch (ExecutionException e) {
            return strategyPolicy.failure(e);
        } catch (TimeoutException e) {
            return strategyPolicy.failure(e);
        } finally {
            threadPoolExecutor.shutdownNow();
        }
    }
}

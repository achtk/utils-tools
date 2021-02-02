package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfoImpl;
import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.limit.LimiterProvider;
import com.chua.utils.tools.limit.TokenLimitProvider;
import com.chua.utils.tools.logger.LogUtils;
import com.chua.utils.tools.storage.*;
import com.chua.utils.tools.time.Cost;
import com.chua.utils.tools.time.MillisecondCost;
import com.chua.utils.tools.util.CollectionUtils;
import com.chua.utils.tools.util.ThreadUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class StorageExample {

    private static CacheProvider<String, Object> CACHE_PROVIDER = new ConcurrentCacheProvider<>();

    public static void main(String[] args) {
        //测试缓存
        testCacheStorage();
        //测试重试
        testRetryStorage();
        //测试代理
        testProxyStorage(TDemoInfoImpl.class);
        //测试限流
        testLimitStorage(2, 1);
        //测试调度
        testTimerStorage();
        //测试异步
        testAsyncStorage();
        //测试回调
        testFailureStorage();
        //测试超时
        testTimeoutStorage();
        //测试延迟
        testDelayStorage();
    }

    /**
     * 测试超时
     */
    private static void testTimeoutStorage() {
        Integer integer = TimeoutStorage.run(() -> {
            ThreadUtils.sleepMillisecondsQuietly(200);
            return 1;
        }, 100);

        LogUtils.info("timeout: {}", integer);
    }

    /**
     * 测试回调
     */
    private static void testFailureStorage() {
        Integer integer = CallbackStorage.run(() -> 2, e -> 1);
        LogUtils.info("callback: {}", integer);
    }

    /**
     * 测试异步
     */
    private static void testAsyncStorage() {
        AsyncStorage.run(() -> {
            ThreadUtils.sleepSecondsQuietly(1);
            System.out.println("async: async");
            return null;
        });
        System.out.println(2222);
    }

    /**
     * 测试延迟
     */
    private static void testDelayStorage() {
        Cost cost = new MillisecondCost("delay");
        LogUtils.info("delay: {}", DelayStorage.run(() -> {
            return 1;
        }, 1000));

        cost.stopAndPrint();
    }

    /**
     * 测试调度
     */
    private static void testTimerStorage() {
        //        TimerStorage.doWith(item -> {
//            LogUtils.info("{}: {}", DateUtils.currentString(), IdUtils.createUuid());
//        }, "0/1 * * * * ?");
    }

    /**
     * 测试代理
     *
     * @param tDemoInfoClass
     */
    private static void testProxyStorage(Class<TDemoInfoImpl> tDemoInfoClass) {
        TDemoInfoImpl with = ProxyStorage.run(tDemoInfoClass, (obj, method, args, proxy) -> {
            method.setAccessible(true);
            return method.invoke(obj, args);
        });

        LogUtils.info("proxy: {}", with.getUuid());
    }

    /**
     * 测试限流
     *
     * @param token
     * @param threadNum
     */
    private static void testLimitStorage(int token, int threadNum) {
        LimiterProvider limiterProvider = new TokenLimitProvider(token);
        ThreadUtils.sleepSecondsQuietly(1);
        ExecutorService executorService = ThreadUtils.newFixedThreadExecutor(threadNum);
        CollectionUtils.forEach(threadNum, item -> {
            executorService.execute(() -> {
                LogUtils.info("limit: {}", LimitStorage.run(() -> {
                    return 1;
                }, limiterProvider));
            });
        });
        executorService.shutdown();

    }

    /**
     * 测试重试
     */
    private static void testRetryStorage() {
        AtomicInteger count = new AtomicInteger();
        LogUtils.info("retry: {}", RetryStorage.doWithFixedWaitResult(() -> {
            return count.incrementAndGet();
        }, 11, item -> {
            return item < 10;
        }));
    }

    /**
     * 测试缓存
     */
    private static void testCacheStorage() {
        CacheStorage.run(() -> {
            return "233";
        }, "demo", CACHE_PROVIDER);

        LogUtils.info("cache: {}", CACHE_PROVIDER.get("demo"));
    }
}

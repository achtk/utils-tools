package com.chua.utils.tools.storage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 均衡机制
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class TimeoutStorage {

    /**
     * 运行任务
     *
     * @param supplier 回调
     * @param timeout  超时(ms)
     * @param <T>      类型
     * @return 均衡对象
     */
    public static <T> T run(Supplier<T> supplier, long timeout) {
        return run(supplier, timeout, null);
    }

    /**
     * 运行任务
     *
     * @param supplier 回调
     * @param timeout  超时(ms)
     * @param failure  异常回调
     * @param <T>      类型
     * @return 均衡对象
     */
    public static <T> T run(Supplier<T> supplier, long timeout, Supplier<T> failure) {
        if (null == supplier) {
            return null;
        }
        CompletableFuture<T> tCompletableFuture = CompletableFuture.supplyAsync(supplier);
        try {
            return tCompletableFuture.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return null == failure ? null : failure.get();
        }
    }
}

package com.chua.utils.tools.storage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * 异步
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class AsyncStorage {

    private AsyncStorage() {
    }

    /**
     * 处理任务
     *
     * @param supplier 回调
     * @return 结果
     */
    public static void run(final Supplier<? extends Runnable> supplier) {
        CompletableFuture.runAsync(supplier.get());
    }

    /**
     * 处理任务
     *
     * @param supplier 回调
     * @return 结果
     */
    public static <T> T runAsync(final Supplier<T> supplier) throws ExecutionException, InterruptedException {
        CompletableFuture<T> completableFuture = CompletableFuture.supplyAsync(supplier);
        return completableFuture.get();
    }
}

package com.chua.utils.tools.strategy.helper;

import com.chua.utils.tools.strategy.handler.IStrategyPolicy;
import com.google.common.base.Preconditions;

import java.util.concurrent.*;

/**
 * 超时机制
 * @author CH
 */
public class TimeoutStrategyHelper<T> {

    /**
     * 超时机制
     * @param strategyPolicy 策略
     * @param timout 超时时间
     * @param delay 延迟时间
     * @return
     */
    public static <T>T timeout(final IStrategyPolicy<T> strategyPolicy, final long timout, final long delay) {
        Preconditions.checkArgument(timout > 0, "time should be bigger than 0");
        Preconditions.checkArgument(null != strategyPolicy, "strategyPolicy should not be null");

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        Future<T> submit = threadPoolExecutor.submit(new Callable<T>() {

            @Override
            public T call() throws Exception {
                return strategyPolicy.policy();
            }
        });
        try {
            return submit.get(timout, TimeUnit.MILLISECONDS);
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

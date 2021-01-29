package com.chua.utils.tools.storage;

import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.limit.LimiterProvider;

import java.util.function.Supplier;

/**
 * 限流
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class LimitStorage {
    /**
     * 处理任务
     *
     * @param supplier        回调
     * @param limiterProvider 限制器
     * @return 结果
     */
    public synchronized static Object run(final Supplier<?> supplier, final LimiterProvider limiterProvider) {
        return run(supplier, StringConstant.DEFAULT, limiterProvider);
    }

    /**
     * 处理任务
     *
     * @param supplier        回调
     * @param group           分组
     * @param limiterProvider 限制器
     * @return 结果
     */
    public synchronized static Object run(final Supplier<?> supplier, final String group, final LimiterProvider limiterProvider) {
        if (null == limiterProvider) {
            return supplier.get();
        }
        synchronized (limiterProvider) {
            if (limiterProvider.tryAcquire(group)) {
                return supplier.get();
            }
        }
        return null;
    }
}

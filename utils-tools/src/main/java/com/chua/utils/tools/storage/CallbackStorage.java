package com.chua.utils.tools.storage;

import com.chua.utils.tools.function.Failure;

import java.util.function.Supplier;

/**
 * 回调
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class CallbackStorage {
    /**
     * 处理任务
     *
     * @param supplier 回调
     * @param failure  异常回调
     * @param <T>      类型
     * @return 结果
     */
    public static <T> T run(Supplier<T> supplier, Failure<T> failure) {
        if (null == supplier) {
            return failure.apply(new NullPointerException());
        }
        try {
            return supplier.get();
        } catch (Exception e) {
            return failure.apply(e);
        }
    }
}

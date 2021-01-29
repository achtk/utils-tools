package com.chua.utils.tools.storage;

import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.util.ObjectUtils;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 缓存暂存器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public class CacheStorage {

    /**
     * 执行并缓存
     *
     * @param supplier      回调
     * @param cacheKey      缓存Key
     * @param cacheProvider 缓存构造器
     * @param <Result>      结果类型
     * @return 结果
     */
    public static <Result> Result run(Supplier<Result> supplier, Object cacheKey, CacheProvider cacheProvider) {
        if (ObjectUtils.isAnyEmpty(cacheKey, cacheProvider, supplier)) {
            return null == supplier ? null : supplier.get();
        }
        Object o = cacheProvider.get(cacheKey);
        if (o != null) {
            return (Result) o;
        }
        Result result = supplier.get();
        cacheProvider.put(cacheKey, result);
        return result;
    }

    /**
     * 执行并缓存
     *
     * @param supplier      回调
     * @param cacheKey      缓存Key
     * @param cacheProvider 缓存构造器
     * @param <Result>      结果类型
     * @return 结果
     */
    public static <Result> Result run(Supplier<Result> supplier, Object cacheKey, Map<? super Object, Object> cacheProvider) {
        if (ObjectUtils.isAnyEmpty(cacheKey, cacheProvider, supplier)) {
            return null == supplier ? null : supplier.get();
        }
        Object o = cacheProvider.get(cacheKey);
        if (o != null) {
            return (Result) o;
        }
        Result result = supplier.get();
        cacheProvider.put(cacheKey, result);
        return result;
    }
}

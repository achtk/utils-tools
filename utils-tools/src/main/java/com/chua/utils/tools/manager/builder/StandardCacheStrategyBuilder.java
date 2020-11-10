package com.chua.utils.tools.manager.builder;

import com.chua.utils.tools.strategy.CacheStrategy;

/**
 * 缓存策略构造器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public class StandardCacheStrategyBuilder<T> implements CacheStrategyBuilder<T> {
    @Override
    public T create(T entity) {
        return new CacheStrategy<T>().create(entity);
    }
}

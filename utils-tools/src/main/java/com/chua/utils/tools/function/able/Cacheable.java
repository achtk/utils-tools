package com.chua.utils.tools.function.able;

import java.util.concurrent.ConcurrentMap;

/**
 * 可被缓存接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/7
 */
public interface Cacheable {
    /**
     * 获取缓存
     *
     * @param aClass 类
     * @param name   缓存名称
     * @param tClass 缓存对象类型
     * @param <T>    缓存对象类型
     * @return 缓存对象
     */
    <T> T get(Class<?> aClass, String name, Class<T> tClass);

    /**
     * 提供缓存配置
     *
     * @return 缓存配置
     */
    default ConcurrentMap<?, ?> get() {
        return null;
    }
}

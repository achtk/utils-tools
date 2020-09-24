package com.chua.utils.tools.limit;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 限流策略
 * @author CH
 */
public interface ILimiterProvider {
    /**
     * 默认限流器配置
     * @return
     */
    default public ILimiterProvider defaultLimiterProvider() {
        return this.newLimiter("default", 10);
    }

    /**
     * 带大小的限流器
     * @param size 最大限制
     * @param name
     * @return
     */
    public ILimiterProvider newLimiter(String name, int size);
    /**
     * 带大小的限流器
     * @param config 最大限制
     * @return
     */
    public ILimiterProvider newLimiter(Map<String, Integer> config);

    /**
     * 尝试获取
     * @param name 资源
     * @return
     */
    public boolean tryAcquire(String name);

    /**
     * 尝试获取
     * @param time 时间
     * @param name 资源
     * @return
     */
    public boolean tryAcquire(String name, long time);
    /**
     * 尝试获取
     * @param time 时间
     * @param name 资源
     * @param timeUnit 类型
     * @return
     */
    public boolean tryAcquire(String name, long time, TimeUnit timeUnit);

    /**
     * 尝试入队
     * @return
     */
    public boolean tryGet();


}

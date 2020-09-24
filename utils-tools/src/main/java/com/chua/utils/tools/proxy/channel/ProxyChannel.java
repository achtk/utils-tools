package com.chua.utils.tools.proxy.channel;

/**
 * 代理管道
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 16:33
 */
public interface ProxyChannel<T> {
    /**
     * 执行方法
     * @param args 参数
     * @return
     */
    public T invoke(Object... args);
}

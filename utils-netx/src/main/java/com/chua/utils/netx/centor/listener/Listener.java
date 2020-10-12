package com.chua.utils.netx.centor.listener;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 配置监听
 * @author CH
 * @version 1.0.0
 * @since 2020/10/12
 */
public interface Listener<T> {

    /**
     * 获取getExecutor
     * @return
     */
    ExecutorService getExecutor();

    /**
     * 接收数据
     * @param configInfo
     */
    void receiveConfigInfo(final String configInfo);

    /**
     * 监听
     * @param t
     */
    void onEvent(T t);
}

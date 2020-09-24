package com.chua.utils.netx.consumer;

import com.chua.utils.netx.configurator.INetxConfigurator;
import com.chua.utils.netx.factory.INetxFactory;

/**
 * 消费者
 *
 * @author CH
 * @since 1.0
 */
public interface INetxConsumer extends INetxConfigurator {
    /**
     * 工具类
     *
     * @return
     */
    public INetxFactory factory();

    /**
     * 启动
     */
    default public void start() {
        factory().start();
    }

    /**
     * 关闭连接
     *
     * @throws Throwable
     */
    default public void close() throws Throwable {
        factory().close();
    }
}

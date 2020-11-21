package com.chua.utils.netx.resolver.mq;

import com.chua.utils.netx.resolver.entity.NetPubSubConf;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * 订阅发布
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public interface NetPubSub<T> {
    /**
     * 发布
     *
     * @param netPubSubConf 配置
     * @param data          数据
     * @throws IOException IOException
     */
    void publish(NetPubSubConf<T> netPubSubConf, byte[] data) throws IOException;

    /**
     * 订阅
     *
     * @param netPubSubConf 配置
     * @param consumer      回调
     * @throws IOException IOException
     */
    void consumer(NetPubSubConf<T> netPubSubConf, Consumer<byte[]> consumer) throws IOException;
}

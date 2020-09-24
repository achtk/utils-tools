package com.chua.utils.netx.rpc.resolver;


import com.chua.utils.netx.rpc.config.RpcConsumerConfig;

/**
 * rpc消费者
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 14:14
 */
public interface IRpcConsumer<T> {

    /**
     * 胜场服务
     * @param rpcConsumerConfig 客户端配置
     * @return T
     */
    public T consumer(RpcConsumerConfig<T> rpcConsumerConfig);
}

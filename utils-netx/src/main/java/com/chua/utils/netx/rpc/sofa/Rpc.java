package com.chua.utils.netx.rpc.sofa;

import com.chua.utils.netx.rpc.config.RpcConsumerConfig;
import com.chua.utils.netx.rpc.config.RpcProviderConfig;
import com.chua.utils.netx.rpc.resolver.IRpcConsumer;
import com.chua.utils.netx.rpc.resolver.IRpcProvider;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 14:00
 */
@Getter
@Setter
public class Rpc {
    /**
     * rpc服务消费者
     */
    private IRpcConsumer rpcConsumer;
    /**
     * rpc服务提供者
     */
    private IRpcProvider rpcProvider;

    /**
     * 提供服务
     */
    public void provider(RpcProviderConfig rpcProviderConfig) {
        rpcProvider.provider(rpcProviderConfig);
    }

    /**
     * 消费服务
     */
    public <T>T provider(final RpcConsumerConfig<T> rpcConsumerConfig) {
        return (T) rpcConsumer.consumer(rpcConsumerConfig);
    }

}

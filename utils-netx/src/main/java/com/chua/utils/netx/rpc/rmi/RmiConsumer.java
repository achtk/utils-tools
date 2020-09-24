package com.chua.utils.netx.rpc.rmi;

import com.chua.utils.netx.rpc.config.RpcConsumerConfig;
import com.chua.utils.netx.rpc.resolver.IRpcConsumer;

import java.rmi.Naming;

/**
 * rmi
 * @author CH
 * @version 1.0.0
 * @className RmiConsumer
 * @since 2020/8/1 1:57
 */
public class RmiConsumer<T> implements IRpcConsumer<T> {

    @Override
    public T consumer(RpcConsumerConfig<T> rpcConsumerConfig) {
        String rmi = "rmi://" + rpcConsumerConfig.getDirectUrl() + "/" + rpcConsumerConfig.getInterfaces().getName();
        try {
            return (T) Naming.lookup(rmi);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}

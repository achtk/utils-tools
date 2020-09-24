package com.chua.utils.netx.rpc.resolver;


import com.chua.utils.netx.rpc.config.RpcProviderConfig;

/**
 * rpc提供者
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 14:14
 */
public interface IRpcProvider<T> {
    /**
     * 暴露服务
     * @param rpcProviderConfig
     */
    public T provider(RpcProviderConfig rpcProviderConfig);

    /**
     * 暴露服务
     * @param rpcProviderConfig
     */
    default public void providerAsync(final RpcProviderConfig rpcProviderConfig) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                provider(rpcProviderConfig);
            }
        }).start();
    }
}

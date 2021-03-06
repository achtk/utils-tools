package com.chua.utils.netx.rpc.resolver;


import com.chua.utils.netx.rpc.config.RpcProviderConfig;

/**
 * rpc提供者
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 14:14
 */
public interface RpcProvider<T> {
    /**
     * 暴露服务
     *
     * @param rpcProviderConfig 配置
     * @return T
     */
    T provider(RpcProviderConfig<T> rpcProviderConfig);

    /**
     * 默认端口
     *
     * @return 默认端口
     */
    int getDefaultPort();
}

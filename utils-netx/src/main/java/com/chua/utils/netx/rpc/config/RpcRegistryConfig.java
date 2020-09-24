package com.chua.utils.netx.rpc.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 注册中心
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 13:52
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class RpcRegistryConfig {
    /**
     * 协议
     */
    private String protocol;
    /**
     * 注册中心地址
     */
    private String address;

    public RpcRegistryConfig(String protocol) {
        this.protocol = protocol;
    }

    public RpcRegistryConfig(String protocol, String address) {
        this.protocol = protocol;
        this.address = address;
    }

    /**
     *
     * @return
     */
    public static RpcRegistryConfig newRegistry() {
        return new RpcRegistryConfig();
    }

    /**
     *
     * @param protocol
     * @param address
     * @return
     */
    public static RpcRegistryConfig newRegistry(String protocol, String address) {
        return new RpcRegistryConfig(protocol, address);
    }
    /**
     *
     * @param address
     * @return
     */
    public static RpcRegistryConfig newZookeeper(String address) {
        RpcRegistryConfig rpcRegistryConfig = new RpcRegistryConfig("zookeeper");
        rpcRegistryConfig.setAddress(address);
        return rpcRegistryConfig;
    }
}

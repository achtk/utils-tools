package com.chua.utils.netx.rpc.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布配置
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 13:54
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class RpcProviderConfig<T> extends RpcCommonConfig<T> {
    /**
     * 标识符
     */
    private String id;
    /**
     * 接口类
     */
    private Class<?> interfaces;
    /**
     * 分组
     */
    private String group;

    /**
     * 服务发布延迟,单位毫秒，默认0，配置为-1代表spring加载完毕（通过spring才生效）
     */
    private int delay;
    /**
     * 直连调用地址
     */
    private String directUrl = "localhost";
    /**
     * 权重
     */
    private int weight;
    /**
     * 服务端执行超时时间(毫秒)，不会打断执行线程，只是打印警告
     */
    private int timeout;
    /**
     * 接口实现
     */
    private T ref;

    /**
     * 注册中心
     */
    private List<RpcRegistryConfig> rpcRegistryConfigs;

    /**
     * 协议配置中心
     */
    private List<RpcProtocolConfig> rpcProtocolConfigs;

    /**
     * 方法注册中心
     */
    private List<RpcMethodConfig> rpcMethodConfigs;
    /**
     * 应用配置中心
     */
    private RpcApplicationConfig rpcApplicationConfig = new RpcApplicationConfig();

    public static <T> RpcProviderConfig<T> of(String id, Class<? super T> interfaces, T ref) {
        RpcProviderConfig<T> rpcProviderConfig = new RpcProviderConfig<>();
        rpcProviderConfig.setRef(ref);
        rpcProviderConfig.setId(id);
        rpcProviderConfig.setInterfaces(interfaces);
        return rpcProviderConfig;
    }

    /**
     * 添加注册中心
     *
     * @param protocol
     * @param address
     */
    public void addRegisterConfig(final String protocol, final String address) {
        RpcRegistryConfig rpcRegistryConfig = new RpcRegistryConfig();
        rpcRegistryConfig.setProtocol(protocol);
        rpcRegistryConfig.setAddress(address);
        if (null == rpcRegistryConfigs) {
            rpcRegistryConfigs = new ArrayList<>();
        }
        rpcRegistryConfigs.add(rpcRegistryConfig);
    }

    /**
     * 注册器
     *
     * @param rpcRegistryConfig 注册器
     * @return this
     */
    public RpcProviderConfig<T> addRegistry(RpcRegistryConfig rpcRegistryConfig) {
        if (null == rpcRegistryConfig) {
            return this;
        }
        if (null == rpcRegistryConfigs) {
            rpcRegistryConfigs = new ArrayList<>();
        }

        rpcRegistryConfigs.add(rpcRegistryConfig);
        return this;
    }

    /**
     * 协议
     *
     * @param rpcProtocolConfig 协议
     * @return this
     */
    public RpcProviderConfig<T> addProtocol(RpcProtocolConfig rpcProtocolConfig) {
        if (null == rpcProtocolConfig) {
            return this;
        }
        if (null == rpcProtocolConfigs) {
            rpcProtocolConfigs = new ArrayList<>();
        }

        rpcProtocolConfigs.add(rpcProtocolConfig);
        return this;
    }

    /**
     * 添加服务中心
     *
     * @param protocols 协议
     */
    public RpcProviderConfig<T> addRpcServerConfig(final String... protocols) {

        for (String protocol : protocols) {
            int port = 0;
            if ("dubbo".equalsIgnoreCase(protocol)) {
                port = 20880;
            } else if ("rmi".equalsIgnoreCase(protocol)) {
                port = 1099;
            } else if ("bolt".equalsIgnoreCase(protocol)) {
                port = 12200;
            } else if ("rest".equalsIgnoreCase(protocol)) {
                port = 12001;
            } else if ("http".equalsIgnoreCase(protocol)) {
                port = 8999;
            }

            addRpcServerConfig(protocol, null, port);
        }
        return this;
    }

    /**
     * 添加服务中心
     *
     * @param protocol 协议
     * @param port     端口
     */
    public void addRpcServerConfig(final String protocol, final int port) {
        addRpcServerConfig(protocol, null, port);
    }

    /**
     * 添加服务中心
     *
     * @param protocol 协议
     * @param host     地址
     * @param port     端口
     */
    public void addRpcServerConfig(final String protocol, final String host, final int port) {
        RpcProtocolConfig rpcProtocolConfig = new RpcProtocolConfig();
        rpcProtocolConfig.setProtocol(protocol);
        rpcProtocolConfig.setPort(port);
        if (null != host && !"".equals(host)) {
            rpcProtocolConfig.setHost(host);
        }
        if (null == rpcProtocolConfigs) {
            rpcProtocolConfigs = new ArrayList<>();
        }
        rpcProtocolConfigs.add(rpcProtocolConfig);
    }

    /**
     * 新建
     *
     * @return this
     */
    public static <T> RpcProviderConfig<T> newProvider() {
        return new RpcProviderConfig<>();
    }


}

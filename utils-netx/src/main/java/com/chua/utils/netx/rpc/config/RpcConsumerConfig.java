package com.chua.utils.netx.rpc.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 消费者服务
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 13:56
 */
@Getter
@Setter
@Accessors(chain = true)
public class RpcConsumerConfig<T> extends RpcCommonConfig<T> {
    /**
     * 分组
     */
    private String group;
    /**
     * 客户端
     */
    private String client;
    /**
     * 检测服务w
     */
    private boolean check;
    /**
     * 唯一标识
     */
    private String id;
    /**
     * 服务接口
     */
    private Class<?> interfaces;
    /**
     * 服务协议
     *
     * @see
     */
    private String protocol = "rmi";

    /**
     * 是否泛化调用
     */
    protected boolean generic;

    /**
     * 集群处理，默认是failover
     */
    protected String cluster;

    /**
     * 客户端调用超时时间(毫秒)
     */
    protected int timeout = -1;

    /**
     * The Retries. 失败后重试次数
     */
    protected int retries;
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
    private RpcApplicationConfig rpcApplicationConfig;

    public static <T> RpcConsumerConfig<T> of(String id, Class<? super T> tClass) {
        RpcConsumerConfig<T> rpcConsumerConfig = new RpcConsumerConfig<>();
        rpcConsumerConfig.setInterfaces(tClass);
        rpcConsumerConfig.setId(id);
        return rpcConsumerConfig;
    }

    /**
     * @param rpcRegistryConfig 注册器
     * @return this
     */
    public RpcConsumerConfig<T> addRegistry(RpcRegistryConfig rpcRegistryConfig) {
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
     * @param rpcProtocolConfig 协议
     * @return this
     */
    public RpcConsumerConfig<T> addProtocol(RpcProtocolConfig rpcProtocolConfig) {
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
     * 新建
     *
     * @return this
     */
    public static <T> RpcConsumerConfig<T> newConsumer() {
        return new RpcConsumerConfig<>();
    }

}

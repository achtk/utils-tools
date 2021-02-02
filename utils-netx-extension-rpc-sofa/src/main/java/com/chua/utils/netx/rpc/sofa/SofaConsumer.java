package com.chua.utils.netx.rpc.sofa;

import com.alipay.sofa.rpc.config.ApplicationConfig;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.MethodConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import com.chua.utils.netx.rpc.config.RpcApplicationConfig;
import com.chua.utils.netx.rpc.config.RpcConsumerConfig;
import com.chua.utils.netx.rpc.config.RpcMethodConfig;
import com.chua.utils.netx.rpc.config.RpcRegistryConfig;
import com.chua.utils.netx.rpc.resolver.RpcConsumer;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * rpc-consumer
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 15:26
 */
public class SofaConsumer<T> implements RpcConsumer<T> {

    @Override
    public T consumer(RpcConsumerConfig<T> rpcConsumerConfig) {

        ConsumerConfig<T> consumerConfig = createConsumerConfig(rpcConsumerConfig);
        //配置注册中心
        doConfigRegistryConfigure(rpcConsumerConfig, consumerConfig);
        //配置应用
        doConfigApplicationConfigure(rpcConsumerConfig, consumerConfig);
        //配置方法
        doConfigMethodsConfigure(rpcConsumerConfig, consumerConfig);

        return consumerConfig.refer();
    }

    /**
     * 配置方法
     *
     * @param consumerConfig    消费配置
     * @param rpcConsumerConfig 消费配置
     */
    private void doConfigMethodsConfigure(RpcConsumerConfig<T> rpcConsumerConfig, ConsumerConfig<T> consumerConfig) {
        List<RpcMethodConfig> rpcMethodConfig = rpcConsumerConfig.getRpcMethodConfigs();
        if (null != rpcMethodConfig && rpcMethodConfig.size() > 0) {

            List<MethodConfig> methodConfigs = new ArrayList<>();

            for (RpcMethodConfig methodConfig : rpcMethodConfig) {
                MethodConfig methodConfig1 = new MethodConfig();
                methodConfig1.setName(methodConfig.getName());
                methodConfig1.setCache(methodConfig.getCache());
                methodConfig1.setParameters(methodConfig.getParameters());
                methodConfig1.setRetries(methodConfig.getRetries());
                methodConfig1.setTimeout(methodConfig.getTimeout());
                methodConfig1.setCompress(methodConfig.getCompress());
                methodConfig1.setConcurrents(methodConfig.getConcurrents());
                methodConfig1.setDstParam(methodConfig.getDstParam());
                methodConfig1.setInvokeType(methodConfig.getInvokeType());
                methodConfig1.setValidation(methodConfig.getValidation());

                methodConfigs.add(methodConfig1);
            }
            consumerConfig.setMethods(methodConfigs);
        }
    }

    /**
     * 配置应用
     *
     * @param consumerConfig    消费配置
     * @param rpcConsumerConfig 消费配置
     */
    private void doConfigApplicationConfigure(RpcConsumerConfig<T> rpcConsumerConfig, ConsumerConfig<T> consumerConfig) {
        RpcApplicationConfig rpcApplicationConfig = rpcConsumerConfig.getRpcApplicationConfig();
        ApplicationConfig applicationConfig = new ApplicationConfig();
        if (null != rpcApplicationConfig) {
            applicationConfig.setAppId(rpcApplicationConfig.getAppId());
            applicationConfig.setAppName(rpcApplicationConfig.getAppName());
            applicationConfig.setInsId(rpcApplicationConfig.getInsId());
        } else {
            applicationConfig.setAppId(UUID.randomUUID().toString());
            applicationConfig.setAppName(UUID.randomUUID().toString());
        }
        consumerConfig.setApplication(applicationConfig);
    }

    /**
     * 创建注册中心
     *
     * @param consumerConfig    消费配置
     * @param rpcConsumerConfig 消费配置
     */
    private void doConfigRegistryConfigure(RpcConsumerConfig<T> rpcConsumerConfig, ConsumerConfig<T> consumerConfig) {
        List<RpcRegistryConfig> rpcRegistryConfigs = rpcConsumerConfig.getRpcRegistryConfigs();
        if (null != rpcRegistryConfigs) {
            List<RegistryConfig> registryConfigs = new ArrayList<>(rpcRegistryConfigs.size());
            for (RpcRegistryConfig config : rpcRegistryConfigs) {
                RegistryConfig registryConfig = new RegistryConfig();
                registryConfig.setProtocol(config.getProtocol());
                registryConfig.setAddress(config.getAddress());

                registryConfigs.add(registryConfig);
            }

            consumerConfig.setRegistry(registryConfigs);
        }
    }

    /**
     * 创建服务
     *
     * @param rpcConsumerConfig 消费配置
     * @return this
     */
    private ConsumerConfig<T> createConsumerConfig(RpcConsumerConfig<T> rpcConsumerConfig) {
        ConsumerConfig<T> consumerConfig = new ConsumerConfig<>();
        consumerConfig.setCheck(rpcConsumerConfig.isCheck());
        consumerConfig.setGeneric(rpcConsumerConfig.isGeneric());
        consumerConfig.setConnectTimeout(rpcConsumerConfig.getTimeout());
        consumerConfig.setCluster(rpcConsumerConfig.getCluster());
        if (!Strings.isNullOrEmpty(rpcConsumerConfig.getProtocol())) {
            consumerConfig.setProtocol(rpcConsumerConfig.getProtocol());
        }

        consumerConfig.setInterfaceId(rpcConsumerConfig.getInterfaces().getName());
        consumerConfig.setRetries(rpcConsumerConfig.getRetries());
        consumerConfig.setId(rpcConsumerConfig.getId());
        consumerConfig.setDirectUrl(rpcConsumerConfig.getDirectUrl());

        return consumerConfig;
    }

    @Override
    public int getDefaultPort() {
        return 12200;
    }
}

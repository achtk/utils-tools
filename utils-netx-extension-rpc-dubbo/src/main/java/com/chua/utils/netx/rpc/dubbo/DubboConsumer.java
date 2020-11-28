package com.chua.utils.netx.rpc.dubbo;

import com.chua.utils.netx.rpc.config.RpcApplicationConfig;
import com.chua.utils.netx.rpc.config.RpcConsumerConfig;
import com.chua.utils.netx.rpc.config.RpcMethodConfig;
import com.chua.utils.netx.rpc.config.RpcRegistryConfig;
import com.chua.utils.netx.rpc.resolver.IRpcConsumer;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.dubbo.config.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 14:42
 */
public class DubboConsumer<T> implements IRpcConsumer<T> {

    @Override
    public T consumer(RpcConsumerConfig<T> rpcConsumerConfig) {

        Preconditions.checkArgument(null != rpcConsumerConfig);
        Preconditions.checkArgument(null != rpcConsumerConfig.getInterfaces());

        ReferenceConfig referenceConfig = createReferenceConfig(rpcConsumerConfig);
        //配置注册中心
        doConfigRegistryConfigure(rpcConsumerConfig, referenceConfig);
        //配置应用
        doConfigApplicationConfigure(rpcConsumerConfig, referenceConfig);
        //配置方法
        doConfigMethodsConfigure(rpcConsumerConfig, referenceConfig);
        //配置消费者
        doConfigConsumerConfigure(rpcConsumerConfig, referenceConfig);

        return (T) referenceConfig.get();
    }

    /**
     * 配置消费者
     * @param rpcConsumerConfig
     * @param referenceConfig
     */
    private void doConfigConsumerConfigure(RpcConsumerConfig<T> rpcConsumerConfig, ReferenceConfig referenceConfig) {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setCheck(rpcConsumerConfig.isCheck());
        referenceConfig.setConsumer(consumerConfig);
    }

    /**
     * 配置方法
     * @param rpcConsumerConfig
     * @param referenceConfig
     */
    private void doConfigMethodsConfigure(RpcConsumerConfig<T> rpcConsumerConfig, ReferenceConfig referenceConfig) {
        List<RpcMethodConfig> rpcMethodConfig = rpcConsumerConfig.getRpcMethodConfigs();
        if(null != rpcMethodConfig && rpcMethodConfig.size() > 0 ) {

            List<MethodConfig> methodConfigs = new ArrayList<>();

            for (RpcMethodConfig methodConfig : rpcMethodConfig) {
                MethodConfig methodConfig1 = new MethodConfig();
                methodConfig1.setName(methodConfig.getName());
                methodConfig1.setParameters(methodConfig.getParameters());
                methodConfig1.setRetries(methodConfig.getRetries());
                methodConfig1.setTimeout(methodConfig.getTimeout());

                methodConfigs.add(methodConfig1);
            }
            referenceConfig.setMethods(methodConfigs);
        }
    }

    /**
     * 配置应用
     * @param rpcConsumerConfig
     * @param referenceConfig
     */
    private void doConfigApplicationConfigure(RpcConsumerConfig<T> rpcConsumerConfig, ReferenceConfig referenceConfig) {
        RpcApplicationConfig rpcApplicationConfig = rpcConsumerConfig.getRpcApplicationConfig();
        ApplicationConfig applicationConfig = new ApplicationConfig();

        if(null != rpcApplicationConfig) {
            applicationConfig.setId(rpcApplicationConfig.getAppId());
            applicationConfig.setName(rpcApplicationConfig.getAppName());

        } else {
            applicationConfig.setId(UUID.randomUUID().toString());
            applicationConfig.setName(UUID.randomUUID().toString());
        }
        referenceConfig.setApplication(applicationConfig);

    }

    /**
     * 配置注册中心
     * @param rpcConsumerConfig
     * @param referenceConfig
     */
    private void doConfigRegistryConfigure(RpcConsumerConfig<T> rpcConsumerConfig, ReferenceConfig referenceConfig) {
        List<RpcRegistryConfig> configs = rpcConsumerConfig.getRpcRegistryConfigs();
        if(null != configs) {
            List<RegistryConfig> registryConfigs = new ArrayList<>(configs.size());
            for (RpcRegistryConfig config : configs) {
                RegistryConfig registryConfig = new RegistryConfig();
                registryConfig.setProtocol(config.getProtocol());
                registryConfig.setAddress(config.getAddress());

                registryConfigs.add(registryConfig);
            }

            referenceConfig.setRegistries(registryConfigs);
        }
    }

    /**
     * 创建服务
     * @param rpcConsumerConfig
     * @return
     */
    private ReferenceConfig createReferenceConfig(RpcConsumerConfig<T> rpcConsumerConfig) {
        ReferenceConfig referenceConfig = new ReferenceConfig();
        referenceConfig.setId(rpcConsumerConfig.getId());
        referenceConfig.setGroup(rpcConsumerConfig.getGroup());
        referenceConfig.setTimeout(rpcConsumerConfig.getTimeout());
        referenceConfig.setInterface(rpcConsumerConfig.getInterfaces());
        if(!Strings.isNullOrEmpty(rpcConsumerConfig.getProtocol())) {
            referenceConfig.setProtocol(rpcConsumerConfig.getProtocol());
        }
        return referenceConfig;
    }
}

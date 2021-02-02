package com.chua.utils.netx.rpc.dubbo;

import com.chua.utils.netx.rpc.config.*;
import com.chua.utils.netx.rpc.resolver.RpcProvider;
import com.google.common.base.Preconditions;
import org.apache.dubbo.config.*;

import java.util.*;

/**
 * dubbo
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 14:30
 */
public class DubboProvider<T> implements RpcProvider<T> {

    @Override
    public T provider(final RpcProviderConfig<T> rpcProviderConfig) {
        Preconditions.checkArgument(null != rpcProviderConfig);
        Preconditions.checkArgument(null != rpcProviderConfig.getRef());

        ServiceConfig<T> serviceConfig = createServiceConfig(rpcProviderConfig);
        //配置注册中心
        doConfigRegistryConfigure(rpcProviderConfig, serviceConfig);
        //配置协议
        doConfigProtocolConfigure(rpcProviderConfig, serviceConfig);
        //配置应用
        doConfigApplicationConfigure(rpcProviderConfig, serviceConfig);
        //配置方法
        doConfigMethodsConfigure(rpcProviderConfig, serviceConfig);
        //暴露
        serviceConfig.export();

        return null;
    }

    @Override
    public int getDefaultPort() {
        return 20880;
    }

    /**
     * 创建服务端配置
     *
     * @param rpcProviderConfig 生产者
     * @return 服务配置
     */
    private ServiceConfig<T> createServiceConfig(final RpcProviderConfig<T> rpcProviderConfig) {
        ServiceConfig<T> serviceConfig = new ServiceConfig<>();
        serviceConfig.setRef(rpcProviderConfig.getRef());
        serviceConfig.setDelay(rpcProviderConfig.getDelay());
        serviceConfig.setId(rpcProviderConfig.getId());
        serviceConfig.setGroup(Optional.ofNullable(rpcProviderConfig.getGroup()).orElse(rpcProviderConfig.getId()));
        serviceConfig.setTimeout(rpcProviderConfig.getTimeout());
        serviceConfig.setWeight(rpcProviderConfig.getWeight());
        serviceConfig.setInterface(rpcProviderConfig.getInterfaces());

        return serviceConfig;
    }

    /**
     * 配置注册中心
     *
     * @param rpcProviderConfig 生产者
     * @param serviceConfig     服务
     */
    private void doConfigRegistryConfigure(final RpcProviderConfig<T> rpcProviderConfig, final ServiceConfig<T> serviceConfig) {
        List<RpcRegistryConfig> configs = rpcProviderConfig.getRpcRegistryConfigs();
        if (null != configs) {
            List<RegistryConfig> registryConfigs = new ArrayList<>(configs.size());
            for (RpcRegistryConfig config : configs) {
                RegistryConfig registryConfig = new RegistryConfig();
                registryConfig.setProtocol(config.getProtocol());
                registryConfig.setAddress(config.getAddress());

                registryConfigs.add(registryConfig);
            }

            serviceConfig.setRegistries(registryConfigs);
            return;
        }
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("multicast");
        registryConfig.setAddress("224.8.8.8:1234");

        serviceConfig.setRegistries(Collections.singletonList(registryConfig));
    }

    /**
     * 配置方法
     *
     * @param rpcProviderConfig 生产者
     * @param serviceConfig     服务
     */
    private void doConfigMethodsConfigure(final RpcProviderConfig<T> rpcProviderConfig, final ServiceConfig<T> serviceConfig) {
        List<RpcMethodConfig> rpcMethodConfig = rpcProviderConfig.getRpcMethodConfigs();
        if (null != rpcMethodConfig && rpcMethodConfig.size() > 0) {
            List<MethodConfig> methodConfigs = new ArrayList<>();

            for (RpcMethodConfig methodConfig : rpcMethodConfig) {
                MethodConfig methodConfig1 = new MethodConfig();
                methodConfig1.setName(methodConfig.getName());
                methodConfig1.setParameters(methodConfig.getParameters());
                methodConfig1.setRetries(methodConfig.getRetries());
                methodConfig1.setTimeout(methodConfig.getTimeout());

                methodConfigs.add(methodConfig1);
            }
            serviceConfig.setMethods(methodConfigs);
        }

    }

    /**
     * 配置应用
     *
     * @param rpcProviderConfig 生产者配置
     * @param serviceConfig     服务配置
     */
    private void doConfigApplicationConfigure(final RpcProviderConfig<T> rpcProviderConfig, final ServiceConfig<T> serviceConfig) {
        RpcApplicationConfig rpcApplicationConfig = rpcProviderConfig.getRpcApplicationConfig();
        ApplicationConfig applicationConfig = new ApplicationConfig();
        if (null != rpcApplicationConfig) {
            applicationConfig.setId(rpcApplicationConfig.getAppId());
            applicationConfig.setName(rpcApplicationConfig.getAppName());
            applicationConfig.setQosEnable(rpcApplicationConfig.isQosEnable());
            applicationConfig.setQosPort(rpcApplicationConfig.getQosPort());
            applicationConfig.setQosAcceptForeignIp(rpcApplicationConfig.isQosAcceptForeignIp());
        } else {
            applicationConfig.setId(UUID.randomUUID().toString());
            applicationConfig.setName(UUID.randomUUID().toString());
            applicationConfig.setQosEnable(false);
        }
        serviceConfig.setApplication(applicationConfig);
    }

    /**
     * 配置协议
     *
     * @param rpcProviderConfig 生产者配置
     * @param serviceConfig     服务配置
     */
    private void doConfigProtocolConfigure(final RpcProviderConfig<T> rpcProviderConfig, final ServiceConfig<T> serviceConfig) {
        List<RpcProtocolConfig> rpcProtocolConfigs = rpcProviderConfig.getRpcProtocolConfigs();
        List<ProtocolConfig> protocolConfigs = new ArrayList<>();

        if (null != rpcProtocolConfigs) {
            for (RpcProtocolConfig config : rpcProtocolConfigs) {
                ProtocolConfig protocolConfig = new ProtocolConfig();
                protocolConfig.setName(config.getProtocol());
                protocolConfig.setPort(config.getPort());
                protocolConfig.setHost(config.getHost());

                protocolConfigs.add(protocolConfig);
            }

        } else {
            ProtocolConfig rmiProtocolConfig = new ProtocolConfig();
            rmiProtocolConfig.setName("rmi");
            rmiProtocolConfig.setPort(1099);
            protocolConfigs.add(rmiProtocolConfig);

            ProtocolConfig dubboProtocolConfig = new ProtocolConfig();
            dubboProtocolConfig.setName("dubbo");
            dubboProtocolConfig.setPort(20880);
            protocolConfigs.add(dubboProtocolConfig);
        }
        serviceConfig.setProtocols(protocolConfigs);
    }

}

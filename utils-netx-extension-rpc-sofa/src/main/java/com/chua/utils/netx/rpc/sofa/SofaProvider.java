package com.chua.utils.netx.rpc.sofa;

import com.alipay.sofa.rpc.config.*;
import com.chua.utils.netx.rpc.config.*;
import com.chua.utils.netx.rpc.resolver.RpcProvider;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/5/30 14:24
 */
public class SofaProvider implements RpcProvider {


    @Override
    public Object provider(RpcProviderConfig rpcProviderConfig) {
        Preconditions.checkArgument(null != rpcProviderConfig);
        Preconditions.checkArgument(null != rpcProviderConfig.getRef());

        ProviderConfig providerConfig = createProviderConfig(rpcProviderConfig);
        //配置注册中心
        doConfigRegistryConfigure(rpcProviderConfig, providerConfig);
        //配置协议
        doConfigProtocolConfigure(rpcProviderConfig, providerConfig);
        //配置应用
        doConfigApplicationConfigure(rpcProviderConfig, providerConfig);
        //配置方法
        doConfigMethodsConfigure(rpcProviderConfig, providerConfig);

        providerConfig.export();

        return null;
    }

    /**
     * 配置方法
     * @param rpcProviderConfig
     * @param providerConfig
     */
    private void doConfigMethodsConfigure(RpcProviderConfig rpcProviderConfig, ProviderConfig providerConfig) {
        List<RpcMethodConfig> rpcMethodConfig = rpcProviderConfig.getRpcMethodConfigs();
        if(null != rpcMethodConfig && rpcMethodConfig.size() > 0) {

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
            providerConfig.setMethods(methodConfigs);
        }
    }

    /**
     * 配置应用
     * @param rpcProviderConfig
     * @param providerConfig
     */
    private void doConfigApplicationConfigure(RpcProviderConfig rpcProviderConfig, ProviderConfig providerConfig) {
        RpcApplicationConfig rpcApplicationConfig = rpcProviderConfig.getRpcApplicationConfig();
        ApplicationConfig applicationConfig = new ApplicationConfig();
        if(null != rpcApplicationConfig) {
            applicationConfig.setAppId(rpcApplicationConfig.getAppId());
            applicationConfig.setAppName(rpcApplicationConfig.getAppName());
            applicationConfig.setInsId(rpcApplicationConfig.getInsId());
        } else {
            applicationConfig.setAppId(UUID.randomUUID().toString());
            applicationConfig.setAppName(UUID.randomUUID().toString());
        }
        providerConfig.setApplication(applicationConfig);
    }

    /**
     * 创建协议
     * @param rpcProviderConfig
     * @param providerConfig
     */
    private void doConfigProtocolConfigure(RpcProviderConfig rpcProviderConfig, ProviderConfig providerConfig) {
        List<RpcProtocolConfig> rpcProtocolConfigs = rpcProviderConfig.getRpcProtocolConfigs();
        List<ServerConfig> serverConfigs1 = new ArrayList<>(rpcProtocolConfigs.size());

        if(null != rpcProtocolConfigs) {
            for (RpcProtocolConfig config : rpcProtocolConfigs) {
                ServerConfig serverConfig = new ServerConfig();
                serverConfig.setProtocol(config.getProtocol());
                serverConfig.setPort(config.getPort());
                serverConfig.setHost(config.getHost());
                serverConfig.setDaemon(config.isDaemon());

                serverConfigs1.add(serverConfig);
            }
        } else {
            ServerConfig serverConfig = new ServerConfig();
            serverConfig.setProtocol("bolt");
            serverConfig.setPort(12200);

            serverConfigs1.add(serverConfig);
        }
        providerConfig.setServer(serverConfigs1);
    }

    /**
     * 创建注册中心
     * @param rpcProviderConfig
     * @param providerConfig
     */
    private void doConfigRegistryConfigure(RpcProviderConfig rpcProviderConfig, ProviderConfig providerConfig) {
        List<RpcRegistryConfig> rpcRegistryConfigs = rpcProviderConfig.getRpcRegistryConfigs();
        if(null != rpcRegistryConfigs) {
            List<RegistryConfig> registryConfigs = new ArrayList<>(rpcRegistryConfigs.size());
            for (RpcRegistryConfig config : rpcRegistryConfigs) {
                RegistryConfig registryConfig = new RegistryConfig();
                registryConfig.setProtocol(config.getProtocol());
                registryConfig.setAddress(config.getAddress());

                registryConfigs.add(registryConfig);
            }

            providerConfig.setRegistry(registryConfigs);
        }
    }

    /**
     * 创建生产者
     * @param rpcProviderConfig
     * @return
     */
    private ProviderConfig createProviderConfig(RpcProviderConfig rpcProviderConfig) {ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setRef(rpcProviderConfig.getRef());
        providerConfig.setDelay(rpcProviderConfig.getDelay());
        providerConfig.setId(rpcProviderConfig.getId());
        providerConfig.setUniqueId(rpcProviderConfig.getGroup());
        providerConfig.setTimeout(rpcProviderConfig.getTimeout());
        providerConfig.setWeight(rpcProviderConfig.getWeight());
        providerConfig.setInterfaceId(rpcProviderConfig.getInterfaces().getName());

        return providerConfig;
    }
}

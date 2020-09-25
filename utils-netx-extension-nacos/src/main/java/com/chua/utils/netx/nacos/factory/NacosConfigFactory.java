package com.chua.utils.netx.nacos.factory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * nacos客户端工具类
 * @author CH
 */
@RequiredArgsConstructor
@NoArgsConstructor
public class NacosConfigFactory implements INetxFactory<ConfigService> {

    @NonNull
    private NetxProperties netxProperties;
    private ConfigService configService;
    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";
    private static final int DEFAULT_TIME_OUT = 3000;
    /**
     * <ul>
     *     <li>serverAddr: nacos地址 </li>
     * </ul>
     * @param netxProperties
     */
    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public ConfigService client() {
        return configService;
    }

    @Override
    public void start() {
        try {
            this.configService = NacosFactory.createConfigService(netxProperties);
        } catch (NacosException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isStart() {
        return null != this.configService;
    }

    @Override
    public void close() throws Exception {
        if(null != this.configService) {
            this.configService.shutDown();
        }
    }

}

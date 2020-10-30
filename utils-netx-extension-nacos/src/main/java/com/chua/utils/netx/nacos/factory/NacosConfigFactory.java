package com.chua.utils.netx.nacos.factory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.netx.factory.INetFactory;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * nacos客户端工具类
 * @author CH
 */
@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor
public class NacosConfigFactory implements INetFactory<ConfigService> {

    @NonNull
    private NetProperties netProperties;
    private ConfigService configService;
    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";
    private static final int DEFAULT_TIME_OUT = 3000;
    /**
     * <ul>
     *     <li>serverAddr: nacos地址 </li>
     * </ul>
     * @param netProperties
     */
    @Override
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public ConfigService client() {
        return configService;
    }

    @Override
    public void start() {
        log.info(">>>>>>>>>>> NacosConfigFactory Starting to connect");
        try {
            netProperties.put("serverAddr", netProperties.getHostIfOnly());
            this.configService = NacosFactory.createConfigService(netProperties);
            log.info(">>>>>>>>>>> NacosConfigFactory connection complete.");
        } catch (NacosException e) {
            e.printStackTrace();
            log.info(">>>>>>>>>>> NacosConfigFactory connection activation failed.");
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

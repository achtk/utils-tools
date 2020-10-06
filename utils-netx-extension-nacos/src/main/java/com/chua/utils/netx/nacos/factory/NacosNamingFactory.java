package com.chua.utils.netx.nacos.factory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.chua.utils.tools.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
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
public class NacosNamingFactory implements INetxFactory<NamingService> {

    @NonNull
    private NetxProperties netxProperties;
    private NamingService namingService;
    private static final String DEFAULT_CLUSTER_NAME = "DEFAULT_CLUSTER_NAME";

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
    public NamingService client() {
        return namingService;
    }

    @Override
    public void start() {
        log.info(">>>>>>>>>>> NacosNamingFactory Starting to connect");
        try {
            this.namingService = NacosFactory.createNamingService(netxProperties);
            log.info(">>>>>>>>>>> NacosNamingFactory connection complete.");
        } catch (NacosException e) {
            e.printStackTrace();
            log.info(">>>>>>>>>>> NacosNamingFactory connection activation failed.");
        }

    }

    @Override
    public boolean isStart() {
        return null != this.namingService;
    }

    @Override
    public void close() throws Exception {
        if(null != this.namingService) {
            this.namingService.shutDown();
        }
    }


}

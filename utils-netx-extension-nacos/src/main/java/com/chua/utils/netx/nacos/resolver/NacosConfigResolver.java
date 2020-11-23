package com.chua.utils.netx.nacos.resolver;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.NetNodeConf;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.node.NetNode;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.properties.NetProperties;
import lombok.SneakyThrows;

import java.util.Properties;
import java.util.function.Consumer;

/**
 * nacos
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class NacosConfigResolver extends NetResolver<ConfigService> implements NetNode {

    private ConfigService configService;

    @SneakyThrows
    @Override
    public void setProperties(Properties properties) {
        properties.put("serverAddr", MapOperableHelper.getString(properties, "serverAddr", NetProperties.CONFIG_FIELD_HOST, ""));
        this.configService = NacosFactory.createConfigService(properties);
        super.setProperties(properties);
    }

    @Override
    public boolean addNode(NetNodeConf netNodeConf, String data) throws Exception {
        try {
            return configService.publishConfig(netNodeConf.getNode(), netNodeConf.getNodeType(), data);
        } catch (NacosException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getValue(NetNodeConf netNodeConf) throws Exception {
        try {
            return configService.getConfig(netNodeConf.getNode(), netNodeConf.getNodeType(), netNodeConf.getTimeoutMs());
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteNode(NetNodeConf netNodeConf) throws Exception {
        try {
            return configService.removeConfig(netNodeConf.getNode(), netNodeConf.getNodeType());
        } catch (NacosException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existNode(NetNodeConf netNodeConf) throws Exception {
        return null != getValue(netNodeConf);
    }

    @SneakyThrows
    @Override
    public void monitor(NetNodeConf netNodeConf, Consumer consumer) throws Exception {
        configService.addListener(netNodeConf.getNode(), netNodeConf.getNodeType(), new AbstractConfigChangeListener() {
            @Override
            public void receiveConfigChange(ConfigChangeEvent event) {
                consumer.accept(event);
            }
        });
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Service<ConfigService> get() {
        return new Service(configService);
    }
}

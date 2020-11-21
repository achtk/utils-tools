package com.chua.utils.netx.nacos.resolver;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.chua.utils.netx.nacos.factory.NacosNamingFactory;
import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.NetNodeConf;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.node.NetNode;
import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.collects.map.MultiMapOperableHelper;
import com.chua.utils.tools.properties.NetProperties;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * nacos
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class NacosNamingResolver extends NetResolver implements NetNode {

    private NacosNamingFactory nacosNamingFactory;
    private NamingService namingService;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        NetProperties netProperties = new NetProperties();
        netProperties.putAll(properties);
        netProperties.put("serverAddr", MapOperableHelper.getString(properties, "host"));

        this.nacosNamingFactory = new NacosNamingFactory(netProperties);
        this.nacosNamingFactory.start();
        this.namingService = this.nacosNamingFactory.client();
    }

    @Override
    public byte[] addNode(NetNodeConf netNodeConf, byte[] data) throws IOException {
        return new byte[0];
    }

    @Override
    public byte[] deleteNode(NetNodeConf netNodeConf) throws IOException {
        return new byte[0];
    }

    @Override
    public boolean existNode(NetNodeConf netNodeConf) throws IOException {
        return false;
    }

    @Override
    public void monitor(NetNodeConf netNodeConf, Consumer consumer) throws IOException {
        // 监听配置
        try {
            namingService.subscribe(netNodeConf.getNode(), event -> consumer.accept(event));
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public Service get() {
        return new Service(namingService);
    }
}

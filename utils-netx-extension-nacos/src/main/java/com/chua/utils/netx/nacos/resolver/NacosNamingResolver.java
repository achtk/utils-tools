package com.chua.utils.netx.nacos.resolver;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.chua.utils.netx.nacos.factory.NacosNamingFactory;
import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.NetNodeConf;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.node.NetNode;
import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.collects.map.MultiMapOperableHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.properties.NetProperties;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * nacos
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class NacosNamingResolver extends NetResolver<NamingService> implements NetNode {

    private NamingService namingService;

    @SneakyThrows
    @Override
    public void setProperties(Properties properties) {
        properties.put("serverAddr", MapOperableHelper.getString(properties, "serverAddr", NetProperties.CONFIG_FIELD_HOST, ""));
        this.namingService = NamingFactory.createNamingService(properties);
        super.setProperties(properties);
    }


    @Override
    public boolean addNode(NetNodeConf netNodeConf, String data) throws Exception {
        Instance instance = JsonHelper.fromJson(data, Instance.class);
        try {
            namingService.registerInstance(netNodeConf.getNode(), netNodeConf.getNodeType(), instance);
        } catch (NacosException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String getValue(NetNodeConf netNodeConf) throws Exception {
        try {
            List<Instance> instances = namingService.getAllInstances(netNodeConf.getNode(), netNodeConf.getNodeType());
            if (null == instances) {
                return null;
            }
            JsonHelper.toJson(instances.stream().map(instance -> {
                Map<String, Object> item = new HashMap<>();
                item.put("port", instance.getPort());
                item.put("ip", instance.getIp());
                item.put("host", instance.getIp() + ":" + instance.getPort());
                item.put("serverName", instance.getServiceName());
                item.put("clusterName", instance.getClusterName());

                return item;
            }).collect(Collectors.toList()));
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteNode(NetNodeConf netNodeConf) throws IOException {
        try {
            namingService.getAllInstances(netNodeConf.getNode(), netNodeConf.getNodeType()).forEach(instance -> {
                try {
                    namingService.deregisterInstance(netNodeConf.getNode(), netNodeConf.getNodeType(), instance);
                } catch (NacosException e) {
                    e.printStackTrace();
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean existNode(NetNodeConf netNodeConf) throws IOException {
        try {
            return namingService.getAllInstances(netNodeConf.getNode(), netNodeConf.getNodeType()).size() != 0;
        } catch (NacosException e) {
            e.printStackTrace();
            return false;
        }
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
    public Service<NamingService> get() {
        return new Service(namingService);
    }
}

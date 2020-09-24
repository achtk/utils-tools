package com.chua.utils.netx.nacos.context;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.config.listener.impl.PropertiesListener;
import com.chua.unified.function.IConsumer;
import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.nacos.factory.NacosConfigFactory;
import com.chua.utils.netx.nacos.factory.NacosNamingFactory;
import com.chua.utils.tools.common.JsonHelper;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * nacos上下文
 * @author CH
 */
public class NacosContext {

    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";
    private static final int DEFAULT_TIME_OUT = 3000;

    private final NamingService namingService;
    private final ConfigService configService;

    private NetxProperties netxProperties;

    private NacosConfigFactory nacosConfigFactory;
    private NacosNamingFactory nacosNamingFactory;

    public NacosContext(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
        this.nacosNamingFactory = new NacosNamingFactory();
        this.nacosConfigFactory = new NacosConfigFactory();

        this.nacosNamingFactory.configure(netxProperties);
        this.nacosConfigFactory.configure(netxProperties);

        this.namingService = this.nacosNamingFactory.client();
        this.configService = this.nacosConfigFactory.client();
    }
    /**
     * 监听
     * @param dataId 数据索引
     * @param consumer 消费者
     */
    public void monitor(final String dataId, final IConsumer consumer) throws Exception {
        monitor(dataId, null, consumer);
    }
    /**
     * 监听
     * @param dataId 数据索引
     * @param group 分组
     * @param consumer 消费者
     */
    public void monitor(final String dataId, final String group, final IConsumer consumer) throws Exception {
        // 监听配置
        configService.addListener(dataId, group, new PropertiesListener() {
            @Override
            public void innerReceive(Properties properties) {
                consumer.next(properties);
            }
        });

        namingService.subscribe(dataId, group, new EventListener() {
            @Override
            public void onEvent(Event event) {
                consumer.next(event);
            }
        });
    }
    /**
     * 获取子节点
     * @param node 节点
     * @return
     */
    public List<String> getConfigChildren(String node) {
        return getConfigChildren(node, DEFAULT_GROUP, DEFAULT_TIME_OUT);
    }
    /**
     * 获取子节点
     * @param node 节点
     * @param timeout 超时时间
     * @return
     */
    public List<String> getConfigChildren(String node, final int timeout) {
        return getConfigChildren(node, DEFAULT_GROUP, timeout);
    }
    /**
     * 获取子节点
     * @param node 节点
     * @param group 分组
     * @return
     */
    public List<String> getConfigChildren(String node, final String group) {
        return getConfigChildren(node, group, DEFAULT_TIME_OUT);
    }

    /**
     * 获取子节点
     * @param node 节点
     * @param group 分组
     * @param timeout 超时时间
     * @return
     */
    public List<String> getConfigChildren(String node, final String group, final int timeout) {
        try {
            String forPath = this.configService.getConfig(node, group, timeout);
            if (null == forPath) {
                return null;
            }
            return Lists.newArrayList(forPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有服务
     * @param node 即诶单
     * @return
     */
    public List<String> getInstance(String node) {
        try {
            List<Instance> allInstances = this.namingService.getAllInstances(node);
            if (null == allInstances) {
                return null;
            }
            List<String> result = new ArrayList<>(allInstances.size());
            for (Instance instance : allInstances) {
                result.add(JsonHelper.toJson(instance));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 删除配置节点
     * @param dataId 数据索引
     * @param group 分组
     * @throws Exception
     */
    public void removeConfig(final String dataId, final String group) throws Exception {
        this.configService.removeConfig(dataId, group);
    }

    /**
     * 删除服务节点
     * @param dataId 数据索引
     * @param group 分组
     * @param ip 地址
     * @param port 端口
     * @throws Exception
     */
    public void removeService(final String dataId, final String group, final String ip, final int port) throws Exception {
        this.namingService.deregisterInstance(dataId, group, ip, port);
    }
    /**
     * 创建配置节点
     * @param dataId 数据索引
     * @param group 分组
     * @param data 数据
     * @throws Exception
     */
    public void createConfig(final String dataId, final String group, final String data) throws Exception {
        this.configService.publishConfig(dataId, group, data);
    }

    /**
     * 创建服务节点
     * @param dataId 数据索引
     * @param group 分组
     * @param ip 地址
     * @param port 端口
     * @param data 数据
     * @throws Exception
     */
    public void createService(final String dataId, final String group, final String ip, final int port, final String data) throws Exception {
        this.namingService.registerInstance(dataId, group, ip, port, data);
    }

}

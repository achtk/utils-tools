package com.chua.utils.netx.nacos.context;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.config.listener.impl.PropertiesListener;
import com.chua.utils.netx.centor.listener.Listener;
import com.chua.utils.tools.common.BeansHelper;
import com.chua.utils.tools.properties.NetxProperties;
import com.chua.utils.netx.nacos.factory.NacosConfigFactory;
import com.chua.utils.netx.nacos.factory.NacosNamingFactory;
import com.chua.utils.tools.common.JsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * nacos上下文
 *
 * @author CH
 */
public class NacosContext implements AutoCloseable {

    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";
    private static final long DEFAULT_TIME_OUT = 3000;

    private final NamingService namingService;
    private final ConfigService configService;

    private NacosConfigFactory nacosConfigFactory;
    private NacosNamingFactory nacosNamingFactory;

    public NacosContext(NetxProperties netxProperties) {
        netxProperties.put("serverAddr", netxProperties.getHostifOnly());
        this.nacosNamingFactory = new NacosNamingFactory(netxProperties);
        this.nacosConfigFactory = new NacosConfigFactory(netxProperties);

        this.nacosNamingFactory.start();
        this.nacosConfigFactory.start();

        this.namingService = this.nacosNamingFactory.client();
        this.configService = this.nacosConfigFactory.client();
    }

    /**
     * 监听
     *
     * @param dataId 数据索引
     * @param group  分组
     */
    public void addConfigListener(final String dataId, final String group, final Listener<Properties> listener) throws Exception {
        // 监听配置
        configService.addListener(dataId, group, new PropertiesListener() {
            @Override
            public void innerReceive(Properties properties) {
                listener.onEvent(properties);
            }
        });
    }

    /**
     * 监听
     *
     * @param serviceName 分组
     * @param listener    消费者
     */
    public void addNamingListener(final String serviceName, final Listener<Event> listener) throws Exception {
        // 监听配置
        namingService.subscribe(serviceName, new EventListener() {
            @Override
            public void onEvent(Event event) {
                listener.onEvent(event);
            }
        });
    }

    /**
     * 监听
     *
     * @param dataId   数据索引
     * @param group    分组
     * @param listener 消费者
     */
    public void removeConfigListener(final String dataId, final String group, final Listener<Properties> listener) throws Exception {
        // 监听配置
        configService.removeListener(dataId, group, new PropertiesListener() {
            @Override
            public void innerReceive(Properties properties) {
                listener.onEvent(properties);
            }
        });
    }

    /**
     * 监听
     *
     * @param serviceName 分组
     * @param listener    消费者
     */
    public void removeNamingListener(final String serviceName, final Listener<Event> listener) throws Exception {
        // 监听配置
        namingService.unsubscribe(serviceName, new EventListener() {
            @Override
            public void onEvent(Event event) {
                listener.onEvent(event);
            }
        });
    }

    /**
     * 获取子节点
     *
     * @param node 节点
     * @return
     */
    public String getConfig(String node) throws Exception {
        return getConfig(node, DEFAULT_GROUP, DEFAULT_TIME_OUT);
    }

    /**
     * 获取子节点
     *
     * @param node    节点
     * @param timeout 超时时间
     * @return
     */
    public String getConfig(String node, final long timeout) throws Exception {
        return getConfig(node, DEFAULT_GROUP, timeout);
    }

    /**
     * 获取子节点
     *
     * @param node  节点
     * @param group 分组
     * @return
     */
    public String getConfig(String node, final String group) throws Exception {
        return getConfig(node, group, DEFAULT_TIME_OUT);
    }

    /**
     * 获取子节点
     *
     * @param node    节点
     * @param group   分组
     * @param timeout 超时时间
     * @return
     */
    public String getConfig(String node, final String group, final long timeout) throws Exception {
        return this.configService.getConfig(node, group, timeout);
    }

    /**
     * 获取所有服务
     *
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
     *
     * @param dataId 数据索引
     * @param group  分组
     * @return
     * @throws Exception
     */
    public boolean removeConfig(final String dataId, final String group) throws Exception {
        this.configService.removeConfig(dataId, group);
        return true;
    }

    /**
     * 删除服务节点
     *
     * @param dataId 数据索引
     * @param group  分组
     * @param ip     地址
     * @param port   端口
     * @throws Exception
     */
    public void removeService(final String dataId, final String group, final String ip, final int port) throws Exception {
        this.namingService.deregisterInstance(dataId, group, ip, port);
    }

    /**
     * 创建配置节点
     *
     * @param dataId 数据索引
     * @param group  分组
     * @param data   数据
     * @throws Exception
     */
    public void createConfig(final String dataId, final String group, final String data) throws Exception {
        this.configService.publishConfig(dataId, group, data);
    }

    @Override
    public void close() throws Exception {
        if (null != this.nacosConfigFactory) {
            nacosConfigFactory.close();
        }
        if (null != this.nacosNamingFactory) {
            nacosNamingFactory.close();
        }
    }

    /**
     * 获取配置并添加监听
     *
     * @param dataId
     * @param group
     * @param timeoutMs
     * @param listener
     * @return
     */
    public String getConfigAndSignListener(String dataId, String group, long timeoutMs, Listener listener) throws NacosException {
        return this.configService.getConfigAndSignListener(dataId, group, timeoutMs, new com.alibaba.nacos.api.config.listener.Listener() {
            @Override
            public Executor getExecutor() {
                return listener.getExecutor();
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                listener.receiveConfigInfo(configInfo);
            }
        });
    }

    /**
     * 状态
     *
     * @return
     */
    public String getServerStatus() {
        return this.configService.getServerStatus();
    }

    /**
     *
     */
    public void shutDown() throws NacosException {
        this.configService.shutDown();
    }

    /**
     * 创建服务节点
     *
     * @param serviceName 数据索引
     * @param group       分组
     * @param ip          地址
     * @param port        端口
     * @param clusterName 分布式
     * @throws Exception
     */
    public void registerInstance(final String serviceName, final String group, final String ip, final int port, final String clusterName) throws Exception {
        this.namingService.registerInstance(serviceName, group, ip, port, clusterName);
    }
    /**
     * 创建服务节点
     *
     * @param serviceName 数据索引
     * @param group       分组
     * @param ip          地址
     * @param port        端口
     * @param clusterName 分布式
     * @throws Exception
     */
    public void deregisterInstance(final String serviceName, final String group, final String ip, final int port, final String clusterName) throws Exception {
        this.namingService.deregisterInstance(serviceName, group, ip, port, clusterName);
    }
    /**
     * 获取服务节点
     *
     * @param serviceName 数据索引
     * @throws Exception
     * @return
     */
    public List<Map<String, Object>> selectInstances(final String serviceName, boolean healthy) throws Exception {
        List<Instance> instances = this.namingService.selectInstances(serviceName, healthy);
        return instence2Map(instances);
    }
    /**
     * 获取服务节点
     *
     * @param serviceName 数据索引
     * @throws Exception
     * @return
     */
    public List<Map<String, Object>> getAllInstances(final String serviceName) throws Exception {
        List<Instance> allInstances = this.namingService.getAllInstances(serviceName);
        return instence2Map(allInstances);
    }

    /**
     *
     * @param allInstances
     * @return
     */
    private List<Map<String, Object>> instence2Map(List<Instance> allInstances) {
        List<Map<String, Object>> properties = new ArrayList<>(allInstances.size());
        for (Instance instance : allInstances) {
            Map<String, Object> objectMap = BeansHelper.toMap(instance);
            if(null == objectMap) {
                continue;
            }
            properties.add(objectMap);
        }
        return properties;
    }

    /**
     * 创建服务节点
     *
     * @param serviceName 数据索引
     * @param listener    监听
     * @throws Exception
     */
    public void subscribe(final String serviceName, final Listener<Event> listener) throws Exception {
        addNamingListener(serviceName, listener);
    }
    /**
     * 创建服务节点
     *
     * @param serviceName 数据索引
     * @param listener    监听
     * @throws Exception
     */
    public void unsubscribe(final String serviceName, final Listener<Event> listener) throws Exception {
        removeNamingListener(serviceName, listener);
    }
}

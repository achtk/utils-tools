package com.chua.utils.netx.zookeeper.center;

import com.chua.utils.netx.centor.listener.Listener;
import com.chua.utils.netx.centor.service.EventPublishingConfigService;
import com.chua.utils.netx.centor.service.EventPublishingNamingService;
import com.chua.utils.netx.zookeeper.context.ZookeeperContext;
import com.chua.utils.tools.common.ByteHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.function.producer.NetxPropertiesProducer;
import com.chua.utils.tools.properties.NetxProperties;
import com.chua.utils.tools.strategy.handler.IStrategyPolicy;
import com.chua.utils.tools.strategy.helper.StrategyHelper;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * zookeeper数据中台
 *
 * @author CH
 * @date 2020-10-07
 */
@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class ZookeeperConfigurationCenter implements EventPublishingConfigService, EventPublishingNamingService, NetxPropertiesProducer, AutoCloseable {

    private static final String DEFAULT_CONFIG_NODE_NAME = "/center/config";
    private static final String CONFIG_NODE_NAME = "config";
    private static final String DEFAULT_GROUP = "group";
    private static final String CLUSTER_NAME_DEFAULT_GROUP = "cluserName";
    @NonNull
    @Getter
    private NetxProperties netxProperties;
    private ZookeeperContext zookeeperContext;
    private String configName;


    @Override
    public void close() throws Exception {
        this.zookeeperContext.close();
    }

    @Override
    public String getConfig(String dataId, String group, long timeoutMs) throws Exception {
        if (Strings.isNullOrEmpty(dataId)) {
            return null;
        }
        String cacheKey = createCacheKey(dataId, group);
        if (!zookeeperContext.exist(cacheKey)) {
            return null;
        }
        if (timeoutMs < 0) {
            byte[] bytes = zookeeperContext.queryForString(cacheKey);
            return ByteHelper.toString(bytes);
        }
        return StrategyHelper.doWithTimeout(new IStrategyPolicy<String>() {
            @Override
            public String policy() {
                byte[] bytes = zookeeperContext.queryForString(cacheKey);
                return ByteHelper.toString(bytes);
            }

            @Override
            public String failure(Throwable throwable) {
                return null;
            }
        }, timeoutMs);
    }

    @Override
    public String getConfigAndSignListener(String dataId, String group, long timeoutMs, Listener listener) throws Exception {
        String config = getConfig(dataId, group, timeoutMs);
        addListener(dataId, group, listener);
        return config;
    }

    @Override
    public void addListener(String dataId, String group, Listener listener) throws Exception {
        String cacheKey = createCacheKey(dataId, group);
        zookeeperContext.addListener(cacheKey, item -> {
            Executor executor = listener.getExecutor();
            ChildData childData = item.getData();
            executor.execute(() -> listener.receiveConfigInfo(ByteHelper.toString(childData.getData())));
        });
    }

    @Override
    public boolean publishConfig(String dataId, String group, String content) throws Exception {
        if (Strings.isNullOrEmpty(dataId)) {
            log.warn("数据不存在注册失败！");
            return false;
        }
        String newKey = createCacheKey(dataId, group);
        if (zookeeperContext.exist(newKey)) {
            zookeeperContext.delete(newKey);
        }
        zookeeperContext.createEphemeral(newKey, content);
        return true;
    }

    /**
     * 创建索引
     *
     * @param dataId 数据标识
     * @param group  分组
     * @return String
     */
    private String createCacheKey(String dataId, String group) {
        group = StringHelper.defaultIfBlank(group, DEFAULT_GROUP);
        return configName + "/" + dataId + "/" + group;
    }

    /**
     * 创建索引
     *
     * @param dataId 数据标识
     * @param group  分组
     * @return String
     */
    private String createCacheKey(String cluserName, String dataId, String group) {
        cluserName = StringHelper.defaultIfBlank(cluserName, CLUSTER_NAME_DEFAULT_GROUP);
        String newKey = configName + "/" + cluserName + "/" + dataId;
        return null == group ? newKey : newKey + "/" + group;
    }

    @Override
    public boolean removeConfig(String dataId, String group) throws Exception {
        if (Strings.isNullOrEmpty(dataId)) {
            log.warn("数据不存在注册失败！");
            return false;
        }
        String newKey = createCacheKey(dataId, group);
        return !zookeeperContext.exist(newKey) || zookeeperContext.delete(newKey);
    }

    @Override
    public void removeListener(String dataId, String group, Listener listener) {
        String newKey = createCacheKey(dataId, group);
        if (!zookeeperContext.exist(newKey)) {
            return;
        }
        zookeeperContext.removeListener(newKey, item -> {
            Executor executor = listener.getExecutor();
            ChildData childData = item.getData();
            executor.execute(() -> listener.receiveConfigInfo(ByteHelper.toString(childData.getData())));
        });
    }

    @Override
    public String getServerStatus() {
        return zookeeperContext.getStatus();
    }

    @Override
    public void shutDown() throws Exception {
        close();
    }

    @Override
    public void initial(NetxProperties netxProperties) {
        zookeeperContext = new ZookeeperContext(netxProperties);
        this.configName = netxProperties.getProperty(CONFIG_NODE_NAME, DEFAULT_CONFIG_NODE_NAME);
        try {
            this.zookeeperContext.createPersistent(configName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerInstance(String serviceName, String group, String ip, int port, String clusterName) throws Exception {
        if (Strings.isNullOrEmpty(serviceName)) {
            log.warn("数据不存在注册失败！");
            return;
        }
        String newKey = createCacheKey(clusterName, serviceName, group);
        if (zookeeperContext.exist(newKey)) {
            zookeeperContext.delete(newKey);
        }
        Properties properties = new Properties();
        properties.put("ip", ip);
        properties.put("port", port);
        zookeeperContext.createEphemeral(newKey, JsonHelper.toJson(properties));
    }

    @Override
    public void deregisterInstance(String serviceName, String group, String ip, int port, String clusterName) throws Exception {
        if (Strings.isNullOrEmpty(serviceName)) {
            log.warn("数据不存在注册失败！");
            return;
        }
        String newKey = createCacheKey(clusterName, serviceName, group);
        if (!zookeeperContext.exist(newKey)) {
            return;
        }
        zookeeperContext.delete(newKey);
    }

    @Override
    public List<Map<String, Object>> selectInstances(String serviceName, boolean healthy) throws Exception {
        String cacheKey = createCacheKey(CLUSTER_NAME_DEFAULT_GROUP, serviceName, null);
        if (!zookeeperContext.exist(cacheKey)) {
            return null;
        }
        List<String> children = zookeeperContext.getChildren(cacheKey);
        List<Map<String, Object>> result = new ArrayList<>(children.size());
        for (String child : children) {
            byte[] query = zookeeperContext.query(cacheKey, child);
            Map<String, Object> stringObjectMap = JsonHelper.fromJson2Map(query);
            if (null == stringObjectMap) {
                continue;
            }
            result.add(stringObjectMap);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getAllInstances(String serviceName) throws Exception {
        return selectInstances(serviceName, false);
    }

    @Override
    public <T> void subscribe(String serviceName, Listener<T> listener) throws Exception {
        String cacheKey = createCacheKey(null, serviceName, null);
        zookeeperContext.addListener(cacheKey, item -> {
            Executor executor = listener.getExecutor();
            ChildData childData = item.getData();
            executor.execute(() -> listener.receiveConfigInfo(ByteHelper.toString(childData.getData())));
        });
    }

    @Override
    public <T> void unsubscribe(String serviceName, Listener<T> listener) throws Exception {
        String cacheKey = createCacheKey(null, serviceName, null);
        zookeeperContext.removeListener(cacheKey, item -> {
            Executor executor = listener.getExecutor();
            ChildData childData = item.getData();
            executor.execute(() -> listener.receiveConfigInfo(ByteHelper.toString(childData.getData())));
        });
    }
}

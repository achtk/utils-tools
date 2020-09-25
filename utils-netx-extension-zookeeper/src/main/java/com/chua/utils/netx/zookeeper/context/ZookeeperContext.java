package com.chua.utils.netx.zookeeper.context;

import com.chua.unified.function.IConsumer;
import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.netx.zookeeper.consumer.ZookeeperConsumer;
import com.chua.utils.netx.zookeeper.factory.ZookeeperFactory;
import com.chua.utils.tools.common.ByteHelper;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * zookeeper上下文
 * @author CH
 */
@Slf4j
public class ZookeeperContext {

    private final CuratorFramework curatorFramework;
    private INetxFactory<CuratorFramework> zookeeperFactory;

    public ZookeeperContext(NetxProperties netxProperties) {
        this.zookeeperFactory = new ZookeeperFactory(netxProperties);
        this.curatorFramework = this.zookeeperFactory.client();
    }
    /**
     * 创建临时节点
     * @param node 节点
     * @param data 数据
     */
    public void createPersistent(final String node, final String data) throws Exception {
        createNode(node, data, CreateMode.PERSISTENT.name());
    }
    /**
     * 创建临时节点
     * @param node 节点
     */
    public void createPersistent(final String node) throws Exception {
        createNode(node, null, CreateMode.PERSISTENT.name());
    }
    /**
     * 创建临时节点
     * @param node 节点
     * @param data 数据
     */
    public void createEphemeral(final String node, final String data) throws Exception {
        createNode(node, data, CreateMode.EPHEMERAL.name());
    }
    /**
     * 创建临时节点
     * @param node 节点
     */
    public void createEphemeral(final String node) throws Exception {
        createNode(node, null, CreateMode.EPHEMERAL.name());
    }
    /**
     * 创建节点
     * @param node 节点
     * @param data 数据
     * @param mode 类型
     */
    public void createNode(final String node, final String data, final String mode) throws Exception{
        CreateMode createMode = CreateMode.EPHEMERAL;
        if (!Strings.isNullOrEmpty(mode)) {
            try {
                createMode = CreateMode.valueOf(mode);
            } catch (IllegalArgumentException e) {
            }
        }

        ACLBackgroundPathAndBytesable<String> withMode = this.curatorFramework.create().creatingParentsIfNeeded().withMode(createMode);
        if (!Strings.isNullOrEmpty(data)) {
            withMode.forPath(node, ByteHelper.getBytes(data));
        } else {
            withMode.forPath(node);
        }
    }

    /**
     * 获取节点
     * @param node 节点名称
     * @param defaultValue 默认数据
     * @return
     */
    public byte[] query(final String node, final byte[] defaultValue) {
        try {
            return this.curatorFramework.getData().forPath(node);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
    /**
     * 获取节点
     * @param node 节点名称
     * @return
     */
    public byte[] query(final String node) {
        return query(node, null);
    }
    /**
     * 获取节点
     * @param node 节点名称
     * @return
     */
    public byte[] queryForString(final String node, String defaultValue) {
        return query(node, null == defaultValue ? null : defaultValue.getBytes(Charsets.UTF_8));
    }

    /**
     * 事件监听
     * @param node
     * @param consumer
     */
    public void monitor(String node, IConsumer<TreeCacheEvent> consumer) {
        TreeCache treeCache = new TreeCache(this.curatorFramework, node);
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData eventData = event.getData();
                if(null != consumer) {
                    consumer.next(event);
                }
                switch (event.getType()) {
                    case NODE_ADDED:
                        log.warn(node + "节点添加" + eventData.getPath() + "\t添加数据为：" + new String(eventData.getData()));
                        break;
                    case NODE_UPDATED:
                        log.warn(eventData.getPath() + "节点数据更新\t更新数据为：" + new String(eventData.getData()) + "\t版本为：" + eventData.getStat().getVersion());
                        break;
                    case NODE_REMOVED:
                        log.warn(eventData.getPath() + "节点被删除");
                        break;
                    default:
                        break;
                }
            }
        });

        try {
            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取子节点
     * @param node 节点
     * @return
     */
    public List<String> getChildren(String node) {
        try {
            return this.curatorFramework.getChildren().forPath(node);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

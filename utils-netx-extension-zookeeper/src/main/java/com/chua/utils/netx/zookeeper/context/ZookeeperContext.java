package com.chua.utils.netx.zookeeper.context;

import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.netx.zookeeper.factory.ZookeeperFactory;
import com.chua.utils.tools.common.ByteHelper;
import com.chua.utils.tools.function.IConsumer;
import com.chua.utils.tools.properties.NetProperties;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * zookeeper上下文
 *
 * @author CH
 */
@Slf4j
public class ZookeeperContext implements AutoCloseable {

    private final CuratorFramework curatorFramework;
    private final INetFactory<CuratorFramework> netxFactory;

    public ZookeeperContext(NetProperties netProperties) {
        this.netxFactory = new ZookeeperFactory(netProperties);
        this.netxFactory.start();
        this.curatorFramework = this.netxFactory.client();
    }

    /**
     * 创建临时节点
     *
     * @param node 节点
     * @param data 数据
     */
    public void createPersistent(final String node, final String data) throws Exception {
        createNode(node, data, CreateMode.PERSISTENT.name());
    }

    /**
     * 创建临时节点
     *
     * @param node 节点
     */
    public void createPersistent(final String node) throws Exception {
        createNode(node, null, CreateMode.PERSISTENT.name());
    }

    /**
     * 创建临时节点
     *
     * @param node 节点
     * @param data 数据
     */
    public void createEphemeral(final String node, final String data) throws Exception {
        createNode(node, data, CreateMode.EPHEMERAL.name());
    }

    /**
     * 创建临时节点
     *
     * @param node 节点
     */
    public void createEphemeral(final String node) throws Exception {
        createNode(node, null, CreateMode.EPHEMERAL.name());
    }

    /**
     * 创建节点
     *
     * @param node 节点
     * @param data 数据
     * @param mode 类型
     */
    public void createNode(final String node, final String data, final String mode) throws Exception {
        CreateMode createMode = CreateMode.EPHEMERAL;
        if (!Strings.isNullOrEmpty(mode)) {
            try {
                createMode = CreateMode.valueOf(mode);
            } catch (IllegalArgumentException e) {}
        }
        Stat stat = this.curatorFramework.checkExists().forPath(node);
        if (null != stat) {
            log.info("节点[{}]已存在", node);
            return;
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
     *
     * @param parent 节点名称
     * @param child  节点名称
     * @return byte[]
     */
    public byte[] query(final String parent, final String... child) {
        String newKey = parent;
        String join = Joiner.on("/").skipNulls().join(child);
        if (!Strings.isNullOrEmpty(join)) {
            newKey += "/" + join;
        }
        try {
            return this.curatorFramework.getData().forPath(newKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取节点
     *
     * @param node         节点名称
     * @param defaultValue 默认数据
     * @return byte[]
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
     *
     * @param node 节点名称
     * @return byte[]
     */
    public byte[] queryForString(final String node, String defaultValue) {
        return query(node, null == defaultValue ? null : defaultValue.getBytes(Charsets.UTF_8));
    }

    /**
     * 获取节点
     *
     * @param node 节点名称
     * @return byte[]
     */
    public byte[] queryForString(final String node) {
        return queryForString(node, null);
    }

    /**
     * 事件监听
     *
     * @param node     节点
     * @param consumer 监听
     */
    public void addListener(String node, IConsumer<TreeCacheEvent> consumer) {
        TreeCache treeCache = new TreeCache(this.curatorFramework, node);
        treeCache.getListenable().addListener((client, event) -> childEventWork(event, node, consumer));

        try {
            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 事件监听
     *
     * @param node     节点
     * @param consumer 监听
     */
    public void removeListener(String node, IConsumer<TreeCacheEvent> consumer) {
        TreeCache treeCache = new TreeCache(this.curatorFramework, node);
        treeCache.getListenable().removeListener((client, event) -> childEventWork(event, node, consumer));

        try {
            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 事件处理
     *
     * @param event    事件
     * @param node     节点
     * @param consumer 回调
     */
    private void childEventWork(TreeCacheEvent event, String node, IConsumer<TreeCacheEvent> consumer) {
        ChildData eventData = event.getData();
        if (null != consumer) {
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

    /**
     * 获取子节点
     *
     * @param node 节点
     * @return List<String>
     */
    public List<String> getChildren(String node) {
        try {
            return this.curatorFramework.getChildren().forPath(node);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        if (null != this.netxFactory) {
            netxFactory.close();
        }
    }

    /**
     * 存在节点
     *
     * @param node 节点
     * @return boolean
     */
    public boolean exist(String node) {
        try {
            Stat stat = this.curatorFramework.checkExists().forPath(node);
            return null != stat;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除节点
     *
     * @param node 节点
     * @return boolean
     */
    public boolean delete(String node) {
        try {
            this.curatorFramework.delete().deletingChildrenIfNeeded().forPath(node);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 服务器状态
     *
     * @return String
     */
    public String getStatus() {
        return this.curatorFramework.getState().toString();
    }
}

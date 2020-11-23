package com.chua.utils.netx.zookeeper.resolver;

import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.NetNodeConf;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.node.NetNode;
import com.chua.utils.netx.zookeeper.context.ZookeeperContext;
import com.chua.utils.tools.common.ByteHelper;
import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.function.IConsumer;
import com.chua.utils.tools.properties.NetProperties;
import lombok.NoArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * zk
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@NoArgsConstructor
public class ZookeeperResolver extends NetResolver<CuratorFramework> implements NetNode<TreeCacheEvent> {

    private CuratorFramework curatorFramework;
    private ZookeeperContext zookeeperContext;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        NetProperties netProperties = new NetProperties();
        netProperties.putAll(properties);

        this.zookeeperContext = new ZookeeperContext(netProperties);
        this.curatorFramework = zookeeperContext.getCuratorFramework();
    }

    @Override
    public Service<CuratorFramework> get() {
        return new Service(curatorFramework);
    }

    @Override
    public boolean addNode(NetNodeConf netNodeConf, String data) throws IOException {
        try {
            zookeeperContext.createNode(netNodeConf.getNode(), data, netNodeConf.getNodeType());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String getValue(NetNodeConf netNodeConf) throws Exception {
        return ByteHelper.toString(zookeeperContext.query(netNodeConf.getNode(), EmptyOrBase.EMPTY_BYTES));
    }

    @Override
    public boolean deleteNode(NetNodeConf netNodeConf) throws IOException {
        try {
            zookeeperContext.delete(netNodeConf.getNode());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean existNode(NetNodeConf netNodeConf) throws IOException {
        return zookeeperContext.exist(netNodeConf.getNode());
    }

    @Override
    public void monitor(NetNodeConf netNodeConf, Consumer<TreeCacheEvent> consumer) throws IOException {
        zookeeperContext.addListener(netNodeConf.getNode(), new IConsumer<TreeCacheEvent>() {
            @Override
            public void next(TreeCacheEvent item) {
                consumer.accept(item);
            }
        });
    }

    @Override
    public void close() throws Exception {
        this.zookeeperContext.close();
    }
}

package com.chua.utils.netx.zookeeper.consumer;

import com.chua.utils.netx.consumer.INetxConsumer;
import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.netx.zookeeper.factory.ZookeeperFactory;
import com.chua.utils.tools.properties.NetProperties;
import org.apache.curator.framework.CuratorFramework;

/**
 * zk 消费者
 * @author CH
 * @since 1.0
 */
public class ZookeeperConsumer implements INetxConsumer {

    private INetFactory<CuratorFramework> netFactory;

    @Override
    public INetFactory<CuratorFramework> factory() {
        return netFactory;
    }

    @Override
    public void initialConfiguration(NetProperties netProperties) {
        netFactory = new ZookeeperFactory();
        try {
            netFactory.configure(netProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

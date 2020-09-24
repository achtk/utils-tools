package com.chua.utils.netx.zookeeper.consumer;


import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.consumer.INetxConsumer;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.netx.zookeeper.factory.ZookeeperFactory;

/**
 * zk 消费者
 * @author CH
 * @since 1.0
 */
public class ZookeeperConsumer implements INetxConsumer {

    private INetxFactory netxFactory;

    @Override
    public INetxFactory factory() {
        return netxFactory;
    }

    @Override
    public void initialConfiguration(NetxProperties netxProperties) {
        netxFactory = new ZookeeperFactory();
        netxFactory.configure(netxProperties);
    }
}

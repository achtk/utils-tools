package com.chua.utils.netx.redission.factory;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.MapHelper;
import com.google.common.base.Strings;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.concurrent.locks.Lock;

/**
 * Redisson 工具
 * @author CH
 * @version 1.0.0
 * @className RedissonFactory
 * @since 2020/7/27 20:14
 */
public class RedissonFactory implements AutoCloseable, INetxFactory<RedissonClient> {

    private static final String ANY_LOCK = "anyLock";
    private NetxProperties netxProperties;
    private RedissonClient redissonClient;


    /**
     * 获取锁
     * @param name
     * @return
     */
    public Lock getLock(String name) {
        return redissonClient.getLock(Strings.isNullOrEmpty(name) ? ANY_LOCK : name);
    }

    @Override
    public void close() throws Exception {
        if(null != redissonClient) {
            redissonClient.shutdown();
        }
    }

    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public RedissonClient client() {
        return redissonClient;
    }

    @Override
    public void start() {
        String[] addresses = netxProperties.getHost();
        Config config = new Config();

        if(BooleanHelper.hasLength(addresses, 2)) {
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            for (String address : addresses) {
                clusterServersConfig.addNodeAddress(address);
            }
            clusterServersConfig.setClientName("cluster");

            if(netxProperties.getConnectTimeout() > 0) {
                clusterServersConfig.setConnectTimeout(netxProperties.getConnectTimeout());
            }

            if(!Strings.isNullOrEmpty(netxProperties.getPassword())) {
                clusterServersConfig.setPassword(netxProperties.getPassword());
            }

            if(!Strings.isNullOrEmpty(netxProperties.getUsername())) {
                clusterServersConfig.setUsername(netxProperties.getUsername());
            }

            if(BooleanHelper.isValid(netxProperties, "scanInterval")) {
                clusterServersConfig.setScanInterval(MapHelper.ints("scanInterval", netxProperties));
            }

            this.redissonClient = Redisson.create(config);
        } else if(BooleanHelper.hasLength(addresses)){

            SingleServerConfig single = config.useSingleServer().setAddress(addresses[0]).setClientName("single");
            if(netxProperties.getConnectTimeout() > 0) {
                single.setConnectTimeout(netxProperties.getConnectTimeout());
            }

            if(!Strings.isNullOrEmpty(netxProperties.getPassword())) {
                single.setPassword(netxProperties.getPassword());
            }

            if(!Strings.isNullOrEmpty(netxProperties.getUsername())) {
                single.setUsername(netxProperties.getUsername());
            }

            this.redissonClient = Redisson.create(config);
        }
    }

    @Override
    public boolean isStart() {
        return !redissonClient.isShutdown();
    }
}

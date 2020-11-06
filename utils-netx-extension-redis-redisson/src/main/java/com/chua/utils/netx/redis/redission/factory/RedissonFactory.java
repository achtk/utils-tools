package com.chua.utils.netx.redis.redission.factory;

import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.collects.map.MapHelper;
import com.google.common.base.Strings;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class RedissonFactory implements AutoCloseable, INetFactory<RedissonClient> {

    private static final String ANY_LOCK = "anyLock";
    @NonNull
    private NetProperties netProperties;
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
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public RedissonClient client() {
        return redissonClient;
    }

    @Override
    public void start() {
        log.info(">>>>>>>>>>> RedissonFactory Starting to connect");
        String[] addresses = netProperties.getHost();
        Config config = new Config();

        if(BooleanHelper.hasLength(addresses, 2)) {
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            for (String address : addresses) {
                clusterServersConfig.addNodeAddress(address);
            }
            clusterServersConfig.setClientName("cluster");

            if(netProperties.getConnectTimeout() > 0) {
                clusterServersConfig.setConnectTimeout(netProperties.getConnectTimeout());
            }

            if(!Strings.isNullOrEmpty(netProperties.getPassword())) {
                clusterServersConfig.setPassword(netProperties.getPassword());
            }

            if(!Strings.isNullOrEmpty(netProperties.getUsername())) {
                clusterServersConfig.setUsername(netProperties.getUsername());
            }

            if(BooleanHelper.isValid(netProperties, "scanInterval")) {
                clusterServersConfig.setScanInterval(MapHelper.ints("scanInterval", netProperties));
            }

            try {
                this.redissonClient = Redisson.create(config);
                log.info(">>>>>>>>>>> RedissonFactory connection complete.");
            } catch (Exception e) {
                e.printStackTrace();
                log.info(">>>>>>>>>>> RedissonFactory connection activation failed.");
            }
        } else if(BooleanHelper.hasLength(addresses)){

            SingleServerConfig single = config.useSingleServer().setAddress(addresses[0]).setClientName("single");
            if(netProperties.getConnectTimeout() > 0) {
                single.setConnectTimeout(netProperties.getConnectTimeout());
            }

            if(!Strings.isNullOrEmpty(netProperties.getPassword())) {
                single.setPassword(netProperties.getPassword());
            }

            if(!Strings.isNullOrEmpty(netProperties.getUsername())) {
                single.setUsername(netProperties.getUsername());
            }

            try {
                this.redissonClient = Redisson.create(config);
                log.info(">>>>>>>>>>> RedissonFactory connection complete.");
            } catch (Exception e) {
                e.printStackTrace();
                log.info(">>>>>>>>>>> RedissonFactory connection activation failed.");
            }
        }
    }

    @Override
    public boolean isStart() {
        return !redissonClient.isShutdown();
    }
}

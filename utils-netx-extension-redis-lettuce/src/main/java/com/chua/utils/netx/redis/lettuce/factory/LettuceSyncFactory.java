package com.chua.utils.netx.redis.lettuce.factory;

import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.common.BooleanHelper;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.sync.RedisAdvancedClusterCommands;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * 同步redis
 *
 * @author CH
 * @version 1.0.0
 * @className RedistSyncFactory
 * @since 2020/6/1 23:32
 */
@Slf4j
@NoArgsConstructor
public class LettuceSyncFactory implements INetFactory {
    private RedisAdvancedClusterCommands<String, String> clusterCommands;
    private RedisCommands<String, String> redisCommands;
    private NetProperties netProperties;

    public LettuceSyncFactory(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public Object client() {
        return null != redisCommands ? redisCommands : clusterCommands;
    }

    @Override
    public void start() {
        log.info(">>>>>>>>>>> LettuceSyncFactory Starting to connect");

        String[] hosts = netProperties.getHost();
        ArrayList<RedisURI> list = new ArrayList<>();
        if (hosts.length > 1) {
            for (String s : hosts) {
                list.add(RedisURI.create(s));
            }
            RedisClusterClient client = RedisClusterClient.create(list);
            try {
                StatefulRedisClusterConnection<String, String> connect = client.connect();
                this.clusterCommands = connect.sync();
                log.info(">>>>>>>>>>> LettuceSyncFactory connection complete.");
            } catch (Exception e) {
                e.printStackTrace();
                log.info(">>>>>>>>>>> LettuceSyncFactory connection activation failed.");
            }
        } else if (BooleanHelper.hasLength(hosts)) {
            RedisClient client = RedisClient.create(hosts[0]);
            try {
                StatefulRedisConnection<String, String> connect = client.connect();
                this.redisCommands = connect.sync();
                log.info(">>>>>>>>>>> LettuceSyncFactory connection complete.");
            } catch (Exception e) {
                e.printStackTrace();
                log.info(">>>>>>>>>>> LettuceSyncFactory connection activation failed.");
            }
        }
    }

    @Override
    public boolean isStart() {
        return null != redisCommands || null != clusterCommands;
    }

    @Override
    public void close() throws Exception {
        if (null != this.redisCommands) {
            redisCommands.close();
        }
        if (null != this.clusterCommands) {
            clusterCommands.close();
        }
    }
}

package com.chua.utils.netx.redis.factory;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.common.BooleanHelper;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.sync.RedisAdvancedClusterCommands;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * 同步redis
 * @author CH
 * @version 1.0.0
 * @className RedistSyncFactory
 * @since 2020/6/1 23:32
 */
@NoArgsConstructor
public class RedisSyncFactory implements INetxFactory {
    private RedisAdvancedClusterCommands<String, String> clusterCommands;
    private RedisCommands<String, String> redisCommands;
    private NetxProperties netxProperties;

    public RedisSyncFactory(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public Object client() {
        return null != redisCommands ? redisCommands : clusterCommands;
    }

    @Override
    public void start() {
        String[] hosts = netxProperties.getHost();
        ArrayList<RedisURI> list = new ArrayList<>();
        if(hosts.length > 1) {
            for (String s : hosts) {
                list.add(RedisURI.create(s));
            }
            RedisClusterClient client = RedisClusterClient.create(list);
            StatefulRedisClusterConnection<String, String> connect = client.connect();
            this.clusterCommands = connect.sync();
        } else if(BooleanHelper.hasLength(hosts)){
            RedisClient client = RedisClient.create(hosts[0]);
            StatefulRedisConnection<String, String> connect = client.connect();
            this.redisCommands = connect.sync();
        }
    }

    @Override
    public boolean isStart() {
        return null != redisCommands || null != clusterCommands;
    }

    @Override
    public void close() throws Exception {
        if(null != this.redisCommands) {
            redisCommands.close();
        }
        if(null != this.clusterCommands) {
            clusterCommands.close();
        }
    }
}

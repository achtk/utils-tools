package com.chua.utils.netx.redis.factory;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.common.BooleanHelper;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.async.RedisAdvancedClusterAsyncCommands;

import java.util.ArrayList;

/**
 * 异步redis
 *
 * @author CH
 * @version 1.0.0
 * @className RedistAsyncFactory
 * @since 2020/6/1 23:32
 */
public class RedisAsyncFactory implements INetxFactory {

    private RedisAdvancedClusterAsyncCommands<String, String> clusterAsyncCommands;
    private RedisAsyncCommands<String, String> redisAsyncCommands;
    private NetxProperties netxProperties;

    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public Object client() {
        return null != redisAsyncCommands ? redisAsyncCommands : clusterAsyncCommands;
    }

    @Override
    public void start() {
        String[] hosts = netxProperties.getHost();
        ArrayList<RedisURI> list = new ArrayList<>();
        if (hosts.length > 1) {
            for (String s : hosts) {
                list.add(RedisURI.create(s));
            }
            RedisClusterClient client = RedisClusterClient.create(list);
            StatefulRedisClusterConnection<String, String> connect = client.connect();
            this.clusterAsyncCommands = connect.async();
        } else if (BooleanHelper.hasLength(hosts)) {
            RedisClient client = RedisClient.create(hosts[0]);
            StatefulRedisConnection<String, String> connect = client.connect();
            this.redisAsyncCommands = connect.async();
        }
    }

    @Override
    public boolean isStart() {
        return null != redisAsyncCommands || null != clusterAsyncCommands;
    }

    @Override
    public void close() throws Exception {
        if (null != this.redisAsyncCommands) {
            redisAsyncCommands.close();
        }
        if (null != this.clusterAsyncCommands) {
            clusterAsyncCommands.close();
        }
    }
}

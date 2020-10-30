package com.chua.utils.netx.redis.lettuce.factory;

import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.common.BooleanHelper;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * 异步redis
 *
 * @author CH
 * @version 1.0.0
 * @className RedistAsyncFactory
 * @since 2020/6/1 23:32
 */
@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class LettuceAsyncFactory implements INetFactory {

    private RedisAdvancedClusterAsyncCommands<String, String> clusterAsyncCommands;
    private RedisAsyncCommands<String, String> redisAsyncCommands;
    @NonNull
    private NetProperties netProperties;

    @Override
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public Object client() {
        return null != redisAsyncCommands ? redisAsyncCommands : clusterAsyncCommands;
    }

    @Override
    public void start() {
        log.info(">>>>>>>>>>> LettuceAsyncFactory Starting to connect");
        String[] hosts = netProperties.getHost();
        ArrayList<RedisURI> list = new ArrayList<>();
        if (hosts.length > 1) {
            for (String s : hosts) {
                list.add(RedisURI.create(s));
            }
            RedisClusterClient client = RedisClusterClient.create(list);
            try {
                StatefulRedisClusterConnection<String, String> connect = client.connect();
                this.clusterAsyncCommands = connect.async();
                log.info(">>>>>>>>>>> LettuceAsyncFactory connection complete.");
            } catch (Exception e) {
                e.printStackTrace();
                log.info(">>>>>>>>>>> LettuceAsyncFactory connection activation failed.");
            }
        } else if (BooleanHelper.hasLength(hosts)) {
            RedisClient client = RedisClient.create(hosts[0]);
            try {
                StatefulRedisConnection<String, String> connect = client.connect();
                this.redisAsyncCommands = connect.async();
                log.info(">>>>>>>>>>> LettuceAsyncFactory connection complete.");
            } catch (Exception e) {
                e.printStackTrace();
                log.info(">>>>>>>>>>> LettuceAsyncFactory connection activation failed.");
            }
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

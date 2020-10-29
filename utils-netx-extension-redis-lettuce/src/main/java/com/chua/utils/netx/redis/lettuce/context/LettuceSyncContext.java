package com.chua.utils.netx.redis.lettuce.context;

import com.chua.utils.netx.function.RKv;
import com.chua.utils.netx.function.RKvProducer;
import com.chua.utils.netx.function.RMq;
import com.chua.utils.netx.function.RMqProducer;
import com.chua.utils.netx.redis.lettuce.function.LettuceRMq;
import com.chua.utils.netx.redis.lettuce.function.LettuceRkv;
import com.chua.utils.tools.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.netx.redis.lettuce.factory.LettuceSyncFactory;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.cluster.api.sync.RedisAdvancedClusterCommands;

/**
 * redis-async 上下文
 *
 * @author CH
 */
public class LettuceSyncContext implements RKvProducer<String, String>, RMqProducer, AutoCloseable {

    private final INetxFactory netxFactory;
    private RedisAdvancedClusterCommands<String, String> clusterCommands;
    private RedisCommands<String, String> redisCommands;

    public LettuceSyncContext(NetxProperties netxProperties) {
        this.netxFactory = new LettuceSyncFactory(netxProperties);
        this.netxFactory.start();
        Object client = this.netxFactory.client();
        if (client instanceof RedisAsyncCommands) {
            this.redisCommands = (RedisCommands<String, String>) client;
            return;
        }
        this.clusterCommands = (RedisAdvancedClusterCommands<String, String>) client;

    }

    @Override
    public void close() throws Exception {
        if (null != this.netxFactory) {
            netxFactory.close();
        }
    }

    @Override
    public RKv<String, String> getKv() {
        return new LettuceRkv(clusterCommands, redisCommands);
    }

    @Override
    public RMq getRmq() {
        return new LettuceRMq(clusterCommands, redisCommands);
    }
}

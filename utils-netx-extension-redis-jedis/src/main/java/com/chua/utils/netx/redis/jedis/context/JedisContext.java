package com.chua.utils.netx.redis.jedis.context;

import com.chua.utils.netx.function.RKv;
import com.chua.utils.netx.function.RKvProducer;
import com.chua.utils.netx.redis.jedis.factory.JedisFactory;
import com.chua.utils.netx.redis.jedis.function.JedisRKv;
import com.chua.utils.tools.properties.NetProperties;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedisPool;

/**
 * jedis上下文
 *
 * @author CH
 * @date 2020-09-28
 */
@Slf4j
public class JedisContext implements RKvProducer<String, String>, AutoCloseable {

    private final JedisFactory netFactory;
    private final ShardedJedisPool shardedJedisPool;
    private NetProperties netProperties;

    public JedisContext(@NonNull NetProperties netProperties) {
        this.netProperties = netProperties;
        this.netFactory = new JedisFactory(netProperties);
        this.netFactory.start();
        this.shardedJedisPool = this.netFactory.client();
    }

    @Override
    public void close() throws Exception {
        if (null != this.netFactory) {
            netFactory.close();
        }
    }

    @Override
    public RKv<String, String> getKv() {
        return new JedisRKv(shardedJedisPool);
    }
}

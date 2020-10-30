package com.chua.utils.netx.redis.jedis.context;

import com.chua.utils.netx.function.RKv;
import com.chua.utils.netx.function.RKvProducer;
import com.chua.utils.netx.redis.jedis.function.JedisRKv;
import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.netx.redis.jedis.factory.JedisFactory;
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

    private final JedisFactory netxFactory;
    private final ShardedJedisPool shardedJedisPool;
    private NetProperties netProperties;

    public JedisContext(@NonNull NetProperties netProperties) {
        this.netProperties = netProperties;
        this.netxFactory = new JedisFactory(netProperties);
        this.netxFactory.start();
        this.shardedJedisPool = this.netxFactory.client();
    }

    @Override
    public void close() throws Exception {
        if (null != this.netxFactory) {
            netxFactory.close();
        }
    }

    @Override
    public RKv<String, String> getKv() {
        return new JedisRKv(shardedJedisPool);
    }
}

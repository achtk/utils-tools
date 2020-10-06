package com.chua.utils.netx.redis.redission.context;

import com.chua.utils.tools.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.netx.redis.redission.factory.RedissonFactory;
import com.google.common.base.Strings;
import org.redisson.api.RedissonClient;

import java.util.concurrent.locks.Lock;

/**
 * redission上下文
 *
 * @author CH
 */
public class RedissonContext implements AutoCloseable {
    private final INetxFactory netxFactory;
    private final RedissonClient redissonClient;
    private static final String ANY_LOCK = "anyLock";


    public RedissonContext(NetxProperties netxProperties) {
        this.netxFactory = new RedissonFactory(netxProperties);
        this.netxFactory.start();
        this.redissonClient = (RedissonClient) this.netxFactory.client();
    }

    /**
     * 获取锁
     *
     * @param name
     * @return
     */
    public Lock getLock(String name) {
        return redissonClient.getLock(Strings.isNullOrEmpty(name) ? ANY_LOCK : name);
    }

    @Override
    public void close() throws Exception {
        if (null != this.netxFactory) {
            netxFactory.close();
        }
    }
}

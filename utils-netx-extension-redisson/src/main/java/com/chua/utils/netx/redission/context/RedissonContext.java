package com.chua.utils.netx.redission.context;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.netx.redission.factory.RedissonFactory;
import com.google.common.base.Strings;
import org.redisson.api.RedissonClient;

import java.util.concurrent.locks.Lock;

/**
 * redission上下文
 * @author CH
 */
public class RedissonContext {
    private final INetxFactory redissionFactory;
    private final RedissonClient redissonClient;
    private static final String ANY_LOCK = "anyLock";


    public RedissonContext(NetxProperties netxProperties) {
        this.redissionFactory = new RedissonFactory(netxProperties);
        this.redissionFactory.configure(netxProperties);
        this.redissonClient = (RedissonClient) this.redissionFactory.client();
    }

    /**
     * 获取锁
     * @param name
     * @return
     */
    public Lock getLock(String name) {
        return redissonClient.getLock(Strings.isNullOrEmpty(name) ? ANY_LOCK : name);
    }

}

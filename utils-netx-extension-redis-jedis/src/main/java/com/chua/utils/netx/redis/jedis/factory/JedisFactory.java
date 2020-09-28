package com.chua.utils.netx.redis.jedis.factory;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.common.MapHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * jedis
 * @author CH
 * @date 2020-09-28
 */
@Slf4j
@RequiredArgsConstructor
public class JedisFactory implements INetxFactory<ShardedJedisPool> {

    @NonNull
    private NetxProperties netxProperties;
    private ShardedJedisPool shardedJedisPool;
    private static ReentrantLock INSTANCE_INIT_LOCL = new ReentrantLock(false);

    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public ShardedJedisPool client() {
        return shardedJedisPool;
    }

    @Override
    public void start() {
        log.info(">>>>>>>>>>> JedisFactory Starting to connect");
        if (shardedJedisPool != null) {
            return;
        }
        try {
            if (INSTANCE_INIT_LOCL.tryLock(2, TimeUnit.SECONDS)) {
                try {
                    initialJedis();
                } finally {
                    INSTANCE_INIT_LOCL.unlock();
                }
            }

        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        if (shardedJedisPool == null) {
            log.info(">>>>>>>>>>> JedisFactory connection activation failed.");
        } else {
            log.info(">>>>>>>>>>> JedisFactory connection complete.");
        }
    }

    /**
     * 初始化Jedis
     */
    private synchronized void initialJedis() {
        if (shardedJedisPool == null) {
            // JedisPoolConfig
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MapHelper.ints(NetxProperties.CONFIG_FIELD_MAX_CONNECTION, 200, netxProperties));
            config.setMaxIdle(50);
            config.setMinIdle(8);
            // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
            config.setMaxWaitMillis(10000);
            // 在获取连接的时候检查有效性, 默认false
            config.setTestOnBorrow(true);
            // 调用returnObject方法时，是否进行有效检查
            config.setTestOnReturn(false);
            // Idle时进行连接扫描
            config.setTestWhileIdle(true);
            // 表示idle object evitor两次扫描之间要sleep的毫秒数
            config.setTimeBetweenEvictionRunsMillis(30000);
            // 表示idle object evitor每次扫描的最多的对象数
            config.setNumTestsPerEvictionRun(10);
            // 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
            config.setMinEvictableIdleTimeMillis(60000);

            String[] addressArr = netxProperties.getHost();

            // JedisShardInfo List
            List<JedisShardInfo> jedisShardInfos = new LinkedList<JedisShardInfo>();

            for (int i = 0; i < addressArr.length; i++) {
                JedisShardInfo jedisShardInfo = new JedisShardInfo(addressArr[i]);
                jedisShardInfos.add(jedisShardInfo);
            }
            this.shardedJedisPool = new ShardedJedisPool(config, jedisShardInfos);
            log.info(">>>>>>>>>>> sso, JedisUtil.ShardedJedisPool init success.");
        }
    }

    @Override
    public boolean isStart() {
        return null != shardedJedisPool && !shardedJedisPool.isClosed();
    }

    @Override
    public void close() throws Exception {
        if (shardedJedisPool != null) {
            shardedJedisPool.close();
        }
    }
}

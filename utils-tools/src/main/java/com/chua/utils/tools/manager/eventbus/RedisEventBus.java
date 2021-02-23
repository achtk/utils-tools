package com.chua.utils.tools.manager.eventbus;

import com.chua.utils.tools.manager.EventMessage;
import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.tools.util.JsonUtils;
import com.chua.utils.tools.util.MapUtils;
import com.chua.utils.tools.util.NetUtils;
import com.chua.utils.tools.util.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * redis-bus
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/23
 */
@Slf4j
public class RedisEventBus implements EventBus, Runnable, AutoCloseable {

    private final List<BusEntity> entities = new ArrayList<>();
    private String topic = "demo";
    private String[] host;
    private Properties properties;
    private ExecutorService executorService = ThreadUtils.newSingleThreadExecutor("redis-bus");
    private ThreadLocal<ShardedJedisPool> shardedJedisPool = new ThreadLocal<ShardedJedisPool>() {
        @Override
        protected ShardedJedisPool initialValue() {
            return null;
        }
    };

    public RedisEventBus(String... host) {
        this.host = host;
        executorService.execute(this);
        this.initialJedis();
    }

    public RedisEventBus(Properties properties) {
        this.properties = properties;
        this.host = MapUtils.getStringArray(properties, "host");
        this.topic = MapUtils.getString(properties, "topic", "demo");
        executorService.execute(this);
        this.initialJedis();
    }

    @Override
    public void register(Object object) {
        entities.add(createMethodMap(object));
    }

    @Override
    public void unregister(Object object) {
        if (null == object) {
            return;
        }
        for (BusEntity entity : entities) {
            if (object.getClass().getName().equals(entity.getName())) {
                entities.remove(entity);
            }
        }
    }

    @Override
    public void post(Object event) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setMessage(event);

        try (ShardedJedis redis = shardedJedisPool.get().getResource()) {
            redis.rpush(topic, JsonUtils.toJson(eventMessage));
        }
    }

    @Override
    public List<BusEntity> getBus() {
        return entities;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                if(null == shardedJedisPool.get()) {
                    this.initialJedis();
                }
                try (ShardedJedis redis = shardedJedisPool.get().getResource()) {
                    String message = redis.lpop(topic);
                    this.subscription(topic, message);
                }
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (null != executorService) {
            executorService.shutdown();
            executorService = null;
        }
    }

    /**
     * 初始化Jedis
     */
    private synchronized void initialJedis() {
        if (shardedJedisPool.get() == null) {
            // JedisPoolConfig
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MapUtils.getIntValue(properties, NetProperties.CONFIG_FIELD_MAX_CONNECTION, 200));
            config.setMaxIdle(MapUtils.getIntValue(properties, "maxIdle", 50));
            config.setMinIdle(MapUtils.getIntValue(properties, "minIdle", 8));
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

            // JedisShardInfo List
            List<JedisShardInfo> jedisShardInfos = new LinkedList<>();

            for (String s : host) {
                JedisShardInfo jedisShardInfo = new JedisShardInfo(NetUtils.getHost(s), NetUtils.getPort(s));
                jedisShardInfos.add(jedisShardInfo);
            }
            shardedJedisPool.remove();
            shardedJedisPool.set(new ShardedJedisPool(config, jedisShardInfos));
            log.info(">>>>>>>>>>> sso, JedisUtil.ShardedJedisPool init success.");
        }
    }

}

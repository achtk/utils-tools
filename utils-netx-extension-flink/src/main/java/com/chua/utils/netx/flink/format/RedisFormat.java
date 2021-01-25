package com.chua.utils.netx.flink.format;

import com.chua.utils.tools.util.NetUtils;
import org.apache.flink.table.descriptors.DescriptorProperties;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.LinkedList;
import java.util.List;

/**
 * redis format
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
public interface RedisFormat {

    /**
     * 初始化Jedis
     *
     * @param sign 信号
     */
    default void initialJedis(String sign) {
        ShardedJedisPool shardedJedisPool1 = FormatConnector.getPool(sign);
        if (null != shardedJedisPool1) {
            return;
        }
        DescriptorProperties descriptorProperties = FormatConnector.getDescriptorProperties(sign);
        // JedisPoolConfig
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(descriptorProperties.getOptionalInt("redis.maxTotal").orElse(200));
        config.setMaxIdle(descriptorProperties.getOptionalInt("redis.maxIdle").orElse(8));
        config.setMinIdle(descriptorProperties.getOptionalInt("redis.minIdle").orElse(1));
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

        String string = descriptorProperties.getOptionalString("redis.host").orElse("127.0.0.1:6379");
        String[] addressArr = string.split(",");

        // JedisShardInfo List
        List<JedisShardInfo> jedisShardInfos = new LinkedList<>();

        for (String s : addressArr) {
            JedisShardInfo jedisShardInfo = new JedisShardInfo(NetUtils.getHost(s), NetUtils.getPort(s));
            jedisShardInfos.add(jedisShardInfo);
        }
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(config, jedisShardInfos);
        FormatConnector.setPool(sign, shardedJedisPool);
    }
}

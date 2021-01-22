package com.chua.utils.netx.flink.connector

import groovy.util.logging.Slf4j
import org.apache.flink.api.common.io.RichInputFormat
import org.apache.flink.api.common.io.statistics.BaseStatistics
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.typeutils.ResultTypeQueryable
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.io.InputSplit
import org.apache.flink.core.io.InputSplitAssigner
import org.apache.flink.types.Row
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.JedisShardInfo
import redis.clients.jedis.ShardedJedisPool

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

import static com.chua.utils.tools.constant.NumberConstant.TWE

/**
 * redis input
 * @author CH* @version 1.0.0* @since 2021/1/22
 */
@Slf4j
class RedisInputFormat extends RichInputFormat<Row, InputSplit> implements ResultTypeQueryable<Row> {

    private Map<String, String> properties;
    private ShardedJedisPool shardedJedisPool
    private static final ReentrantLock INSTANCE_INIT_LOCK = new ReentrantLock(false)


    RedisInputFormat(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    void configure(Configuration parameters) {

    }

    @Override
    BaseStatistics getStatistics(BaseStatistics cachedStatistics) throws IOException {
        return null
    }

    @Override
    InputSplit[] createInputSplits(int minNumSplits) throws IOException {
        return new InputSplit[0]
    }

    @Override
    InputSplitAssigner getInputSplitAssigner(InputSplit[] inputSplits) {
        return null
    }

    @Override
    void open(InputSplit split) throws IOException {

    }

    @Override
    void openInputFormat() throws IOException {
        log.info(">>>>>>>>>>> JedisFactory Starting to connect")
        if (shardedJedisPool != null) {
            return
        }
        try {
            if (INSTANCE_INIT_LOCK.tryLock(TWE, TimeUnit.SECONDS)) {
                try {
                    initialJedis()
                } finally {
                    INSTANCE_INIT_LOCK.unlock()
                }
            }

        } catch (InterruptedException e) {
            log.error(e.getMessage(), e)
        }

        if (shardedJedisPool == null) {
            log.info(">>>>>>>>>>> JedisFactory connection activation failed.")
        } else {
            log.info(">>>>>>>>>>> JedisFactory connection complete.")
        }
    }

    /**
     * 初始化Jedis
     */
    private synchronized void initialJedis() {
        if (shardedJedisPool == null) {
            // JedisPoolConfig
            JedisPoolConfig config = new JedisPoolConfig()
            config.setMaxTotal(200)
            config.setMaxIdle(50)
            config.setMinIdle(8)
            // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
            config.setMaxWaitMillis(10000)
            // 在获取连接的时候检查有效性, 默认false
            config.setTestOnBorrow(true)
            // 调用returnObject方法时，是否进行有效检查
            config.setTestOnReturn(false)
            // Idle时进行连接扫描
            config.setTestWhileIdle(true)
            // 表示idle object evitor两次扫描之间要sleep的毫秒数
            config.setTimeBetweenEvictionRunsMillis(30000)
            // 表示idle object evitor每次扫描的最多的对象数
            config.setNumTestsPerEvictionRun(10)
            // 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
            config.setMinEvictableIdleTimeMillis(60000)

            String[] addressArr = properties[RedisDynamicTableSourceFactory.HOST]

            // JedisShardInfo List
            List<JedisShardInfo> jedisShardInfos = new LinkedList<>()

            for (String s : addressArr) {
                JedisShardInfo jedisShardInfo = new JedisShardInfo(s)
                jedisShardInfos.add(jedisShardInfo)
            }
            this.shardedJedisPool = new ShardedJedisPool(config, jedisShardInfos)
            log.info(">>>>>>>>>>> sso, JedisUtil.ShardedJedisPool init success.")
        }
    }

    @Override
    boolean reachedEnd() throws IOException {
        return false
    }

    @Override
    Row nextRecord(Row reuse) throws IOException {
        return null
    }

    @Override
    void close() throws IOException {
        if (null != shardedJedisPool) {
            shardedJedisPool.close()
        }
    }

    @Override
    void closeInputFormat() throws IOException {
        if (null != shardedJedisPool) {
            shardedJedisPool.close()
        }
    }

    @Override
    TypeInformation<Row> getProducedType() {
        return null
    }
}

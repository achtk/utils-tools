package com.chua.utils.netx.redis.jedis.util;

import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.function.psub.PubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * jedis工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class JedisUtil {
    /**
     * 获取值
     *
     * @param key       索引
     * @param jedisPool jedis
     * @return 获取的值
     */
    public static String getValue(String key, final JedisPool jedisPool) {
        return execute(jedis -> jedis.get(key), jedisPool);
    }

    /**
     * 获取值
     *
     * @param key       索引
     * @param jedisPool jedis
     * @return 获取的值
     */
    public static byte[] getValue(byte[] key, final JedisPool jedisPool) {
        return execute(jedis -> jedis.get(key), jedisPool);
    }

    /**
     * 删除
     *
     * @param key       索引
     * @param jedisPool jedis
     * @return 获取的值
     */
    public static Long del(String key, final JedisPool jedisPool) {
        return execute(jedis -> jedis.del(key), jedisPool);
    }

    /**
     * 设置
     *
     * @param key       索引
     * @param value     值
     * @param jedisPool jedis
     * @return {value}
     */
    public static String set(final String key, final String value, final JedisPool jedisPool) {
        return execute(jedis -> jedis.set(key, value), jedisPool);
    }

    /**
     * 索引是否存在
     *
     * @param key       索引
     * @param jedisPool jedis
     * @return {value}
     */
    public static Boolean exists(final String key, final JedisPool jedisPool) {
        return execute(jedis -> jedis.exists(key), jedisPool);
    }

    /**
     * 索引是否存在
     *
     * @param key       索引
     * @param jedisPool jedis
     * @return {value}
     */
    public static Long exists(final Set<String> key, final JedisPool jedisPool) {
        return execute(jedis -> jedis.exists(key.toArray(EmptyOrBase.EMPTY_STRING)), jedisPool);
    }

    /**
     * 获取消息队列数据长度
     *
     * @param queueName 索引
     * @param jedisPool jedis
     * @return {value}
     */
    public static Long lLen(final String queueName, final JedisPool jedisPool) {
        return execute(jedis -> jedis.llen(queueName), jedisPool);
    }

    /**
     * 设置消息队列数据(左侧)
     *
     * @param queueName 索引
     * @param data      数据
     * @param jedisPool jedis
     * @return 数据长度
     */
    public static Long lPushx(final String queueName, final String data, final JedisPool jedisPool) {
        return execute(jedis -> jedis.lpushx(queueName, data), jedisPool);
    }

    /**
     * 设置消息队列数据(右侧)
     *
     * @param queueName 索引
     * @param data      数据
     * @param jedisPool jedis
     * @return 数据长度
     */
    public static Long rPushx(final String queueName, final String data, final JedisPool jedisPool) {
        return execute(jedis -> jedis.rpush(queueName, data), jedisPool);
    }

    /**
     * 获取消息队列数据(左侧)
     *
     * @param queueName 索引
     * @param jedisPool jedis
     * @return 数据长度
     */
    public static String lPop(final String queueName, final JedisPool jedisPool) {
        return execute(jedis -> jedis.lpop(queueName), jedisPool);
    }

    /**
     * 获取消息队列数据(左侧)
     *
     * @param queueName 索引
     * @param jedisPool jedis
     * @return 数据长度
     */
    public static List<String> blPop(final String queueName, final JedisPool jedisPool) {
        return execute(jedis -> jedis.blpop(queueName), jedisPool);
    }

    /**
     * 获取消息队列数据(左侧)
     *
     * @param queueNames 索引
     * @param jedisPool  jedis
     * @return 数据长度
     */
    public static List<String> blPop(final Set<String> queueNames, final JedisPool jedisPool) {
        return execute(jedis -> jedis.blpop(queueNames.toArray(EmptyOrBase.EMPTY_STRING)), jedisPool);
    }

    /**
     * 获取消息队列数据(右侧)<rpop：非阻塞式>
     *
     * @param queueName 索引
     * @param jedisPool jedis
     * @return 数据长度
     */
    public static String rPop(final String queueName, final JedisPool jedisPool) {
        return execute(jedis -> jedis.rpop(queueName), jedisPool);
    }

    /**
     * 获取消息队列数据(右侧)<brpop：阻塞式>
     *
     * @param queueName 索引
     * @param jedisPool jedis
     * @return 数据长度
     */
    public static List<String> brPop(final String queueName, final JedisPool jedisPool) {
        return execute(jedis -> jedis.brpop(queueName), jedisPool);
    }

    /**
     * 获取消息队列数据(右侧)<brpop：阻塞式>
     *
     * @param queueNames 索引
     * @param jedisPool  jedis
     * @return 数据长度
     */
    public static List<String> brPop(final Set<String> queueNames, final JedisPool jedisPool) {
        return execute(jedis -> jedis.brpop(queueNames.toArray(EmptyOrBase.EMPTY_STRING)), jedisPool);
    }

    /**
     * 索引设置超时时间
     *
     * @param key       索引
     * @param second    索引超时时间
     * @param jedisPool jedis
     * @return 数据长度
     */
    public static Long timeout(final String key, final int second, final JedisPool jedisPool) {
        return execute(jedis -> jedis.expire(key, second), jedisPool);
    }

    /**
     * 获取所有匹配的索引
     *
     * @param key       索引
     * @param jedisPool jedis
     * @return 数据长度
     */
    public static Set<String> keys(final String key, final JedisPool jedisPool) {
        return execute(jedis -> jedis.keys(key), jedisPool);
    }

    /**
     * 查看值
     *
     * @param key       索引
     * @param start     开始
     * @param end       结束 0 到 -1代表所有值
     * @param jedisPool jedis
     * @return 数据长度
     */
    public static List<String> lRange(final String key, final long start, final long end, final JedisPool jedisPool) {
        return execute(jedis -> jedis.lrange(key, start, end), jedisPool);
    }

    /**
     * 发布
     *
     * @param channel   管道
     * @param message   数据
     * @param jedisPool jedis
     * @return 数据长度
     */
    public static Long publish(final String channel, final String message, final JedisPool jedisPool) {
        return execute(jedis -> jedis.publish(channel, message), jedisPool);
    }

    /**
     * 订阅
     *
     * @param pubSub    订阅发布
     * @param channel   管道
     * @param jedisPool jedis
     * @return 数据长度
     */
    public static void subscribe(final PubSub pubSub, final String channel, final JedisPool jedisPool) {
        consumer(jedis -> jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                pubSub.onMessage(channel, message);
            }
        }, channel), jedisPool);
    }

    /**
     * 执行
     *
     * @param function  回调
     * @param jedisPool jedis
     * @param <T>       类型
     * @return 数据
     */
    public static <T> T execute(final Function<Jedis, T> function, final JedisPool jedisPool) {
        if (null == jedisPool || jedisPool.isClosed()) {
            throw new IllegalStateException("redis未连接");
        }
        try (Jedis jedis = jedisPool.getResource()) {
            return function.apply(jedis);
        }
    }

    /**
     * 执行
     *
     * @param consumer  回调
     * @param jedisPool jedis
     * @return 数据
     */
    public static void consumer(final Consumer<Jedis> consumer, final JedisPool jedisPool) {
        if (null == jedisPool || jedisPool.isClosed()) {
            throw new IllegalStateException("redis未连接");
        }
        try (Jedis jedis = jedisPool.getResource()) {
            consumer.accept(jedis);
        }
    }
}

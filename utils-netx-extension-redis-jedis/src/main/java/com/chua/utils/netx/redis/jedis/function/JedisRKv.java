package com.chua.utils.netx.redis.jedis.function;

import com.chua.utils.netx.function.RKv;
import com.chua.utils.tools.common.BooleanHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.*;

/**
 * Jedis-Kv
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
@Slf4j
@AllArgsConstructor
public class JedisRKv implements RKv<String, String> {

    private final ShardedJedisPool shardedJedisPool;

    @Override
    public void set(String key, String value) throws Exception {
        try (ShardedJedis client = shardedJedisPool.getResource()) {
            client.set(key, value);
        }
    }

    @Override
    public String get(String key) throws Exception {
        try (ShardedJedis client = shardedJedisPool.getResource()) {
            return client.get(key);
        }
    }

    @Override
    public void incr(String key, int step, int ttl) throws Exception {
        try (ShardedJedis client = shardedJedisPool.getResource()) {
            client.incrBy(key, step);
        }
    }

    @Override
    public void incr(String key, int step) throws Exception {
        try (ShardedJedis client = shardedJedisPool.getResource()) {
            client.incrBy(key, step);
        }
    }

    @Override
    public void set(Map<String, String> map) throws Exception {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void set(Map<String, String> map, int ttl) throws Exception {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            set(entry.getKey(), entry.getValue(), ttl);
        }
    }

    @Override
    public void set(String key, String value, int ttl) throws Exception {
        try (ShardedJedis client = shardedJedisPool.getResource()) {
            client.setex(key, ttl, value);
        }
    }

    @Override
    public void ttl(String key, int ttl) throws Exception {
        try (ShardedJedis client = shardedJedisPool.getResource()) {
            client.expire(key, ttl);
        }
    }

    @Override
    public Map<String, String> get(List<String> keys) throws Exception {
        Map<String, String> result = new HashMap<>(keys.size());
        for (String key : keys) {
            result.put(key, get(key));
        }
        return result;
    }

    @Override
    public void del(String key) throws Exception {
        try (ShardedJedis client = shardedJedisPool.getResource()) {
            client.del(key);
        }
    }

    @Override
    public void delPrefix(String keyPrefix) throws Exception {
        for (String key : getKeys(keyPrefix)) {
            del(key);
        }
    }

    @Override
    public List<String> keys(String keyPrefix) throws Exception {
        List<String> value = new ArrayList<>();
        for (String key : getKeys(keyPrefix)) {
            value.add(get(key));
        }
        return value;
    }

    @Override
    public List<String> getKeys(String keyPrefix) throws Exception {
        List<String> allKeys = new ArrayList<>();
        try (ShardedJedis client = shardedJedisPool.getResource()) {
            Collection<Jedis> shards = client.getAllShards();
            for (Jedis shard : shards) {
                Set<String> keys = shard.keys(keyPrefix);
                if (BooleanHelper.hasLength(keys)) {
                    allKeys.addAll(keys);
                }
            }
        }
        return allKeys;
    }
}

package com.chua.utils.netx.redis.lettuce.function;

import com.chua.utils.netx.function.RKv;
import com.chua.utils.tools.common.BooleanHelper;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.cluster.api.sync.RedisAdvancedClusterCommands;
import lombok.AllArgsConstructor;

import java.util.*;

/**
 * Lettuce-kv
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
@AllArgsConstructor
public class LettuceRkv implements RKv<String, String> {

    private RedisAdvancedClusterCommands<String, String> clusterCommands;
    private RedisCommands<String, String> redisCommands;

    @Override
    public void set(String key, String value) throws Exception {
        if (null != clusterCommands) {
            clusterCommands.set(key, value);
        } else {
            redisCommands.set(key, value);
        }
    }

    @Override
    public String get(String key) throws Exception {
        if (null != clusterCommands) {
            return clusterCommands.get(key);
        } else {
            return redisCommands.get(key);
        }
    }

    @Override
    public void incr(String key, int step, int ttl) throws Exception {
        if (null != clusterCommands) {
            clusterCommands.incrby(key, step);
        } else {
            redisCommands.incrby(key, step);
        }
        ttl(key, ttl);
    }

    @Override
    public void incr(String key, int step) throws Exception {
        if (null != clusterCommands) {
            clusterCommands.incrby(key, step);
        } else {
            redisCommands.incrby(key, step);
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
        if (null != clusterCommands) {
            clusterCommands.setex(key, ttl, value);
        } else {
            redisCommands.setex(key, ttl, value);
        }
    }

    @Override
    public void ttl(String key, int ttl) throws Exception {
        if (null != clusterCommands) {
            clusterCommands.expire(key, ttl);
        } else {
            redisCommands.expire(key, ttl);
        }
    }

    @Override
    public Map<String, String> get(List<String> keys) throws Exception {
        Map<String, String> result = new HashMap<>();
        for (String key : keys) {
            result.put(key, get(key));
        }
        return result;
    }

    @Override
    public void del(String key) throws Exception {
        if (null != clusterCommands) {
            clusterCommands.del(key);
        } else {
            redisCommands.del(key);
        }
    }

    @Override
    public void delPrefix(String keyPrefix) throws Exception {
        List<String> keys = getKeys(keyPrefix);
        if (!BooleanHelper.hasLength(keys)) {
            return;
        }
        for (String key : keys) {
            del(key);
        }

    }

    @Override
    public List<String> keys(String keyPrefix) throws Exception {
        List<String> keys = getKeys(keyPrefix);
        if (!BooleanHelper.hasLength(keys)) {
            return Collections.emptyList();
        }
        List<String> value = new ArrayList<>();
        for (String key : keys) {
            value.add(get(key));
        }
        return value;
    }

    @Override
    public List<String> getKeys(String keyPrefix) throws Exception {
        if (null != clusterCommands) {
            return clusterCommands.keys(keyPrefix);
        } else {
            return redisCommands.keys(keyPrefix);
        }
    }
}

package com.chua.utils.netx.redis.lettuce.function;

import com.chua.utils.netx.function.RMq;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.cluster.api.sync.RedisAdvancedClusterCommands;
import lombok.AllArgsConstructor;

/**
 * Lettuce-mq
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
@AllArgsConstructor
public class LettuceRMq implements RMq {

    private RedisAdvancedClusterCommands<String, String> clusterCommands;
    private RedisCommands<String, String> redisCommands;


    @Override
    public void lpush(String key, String... value) throws Exception {
        if (null != clusterCommands) {
            clusterCommands.lpush(key, value);
        } else {
            redisCommands.lpush(key, value);
        }
    }

    @Override
    public String lpop(String key) throws Exception {
        if (null != clusterCommands) {
            return clusterCommands.lpop(key);
        } else {
            return redisCommands.lpop(key);
        }
    }
}

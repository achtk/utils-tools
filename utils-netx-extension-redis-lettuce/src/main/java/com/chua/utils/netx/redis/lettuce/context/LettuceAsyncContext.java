package com.chua.utils.netx.redis.lettuce.context;

import com.chua.utils.tools.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.netx.redis.lettuce.factory.LettuceAsyncFactory;
import com.google.common.base.Preconditions;
import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.SetArgs;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.lambdaworks.redis.cluster.api.async.RedisAdvancedClusterAsyncCommands;

import java.util.List;

/**
 * redis-async 上下文
 * @author CH
 */
public class LettuceAsyncContext implements AutoCloseable {

    /**
     * 成功,"OK"
     */
    private final String SUCCESS_OK = "OK";
    /**
     * 成功,1L
     */
    private final Long SUCCESS_STATUS_LONG = 1L;
    /**
     * 只用key不存在时才设置。Only set the key if it does not already exist
     */
    private final String NX = "NX";
    /**
     * XX -- 只有key存在时才设置。和NX相反。Only set the key if it already exist.
     */
    private final String XX = "XX";
    /**
     * EX|PX, 时间单位，EX是秒，PX是毫秒。expire time units: EX = seconds; PX = milliseconds
     */
    private final String EX = "EX";

    private final INetxFactory netxFactory;

    private RedisAdvancedClusterAsyncCommands<String, String> clusterAsyncCommands;
    private RedisAsyncCommands<String, String> redisAsyncCommands;


    public LettuceAsyncContext(NetxProperties netxProperties) {
        this.netxFactory = new LettuceAsyncFactory(netxProperties);
        this.netxFactory.configure(netxProperties);
        this.netxFactory.start();
        Object client = this.netxFactory.client();
        if(client instanceof RedisAsyncCommands) {
            this.redisAsyncCommands = (RedisAsyncCommands<String, String>) client;
            return;
        }
        this.clusterAsyncCommands = (RedisAdvancedClusterAsyncCommands<String, String>) client;

    }
    /**
     * 成功返回true
     *
     * @param key
     * @param value
     * @return
     */
    public RedisFuture<String> set(String key, String value) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.set(key, value);
        } else {
            return redisAsyncCommands.set(key, value);
        }
    }

    /**
     * 发布信息
     *
     * @param key
     * @param value
     * @return
     */
    public void lpush(String key, String... value) {
        if (null != clusterAsyncCommands) {
            clusterAsyncCommands.lpush(key, value);
        } else {
            redisAsyncCommands.lpush(key, value);
        }
    }

    /**
     * 订阅信息
     *
     * @param key
     * @return
     */
    public RedisFuture<String> lpop(String key) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.lpop(key);
        } else {
            return redisAsyncCommands.lpop(key);
        }
    }

    /**
     * 订阅信息
     *
     * @param key
     * @return
     */
    public RedisFuture<List<String>> lrange(String key, long start, long length) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.lrange(key, start, length);
        } else {
            return redisAsyncCommands.lrange(key, start, length);
        }
    }

    /**
     * 获取keys
     *
     * @param key
     * @return
     */
    public RedisFuture<List<String>> keys(String key) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.keys(key);
        } else {
            return redisAsyncCommands.keys(key);
        }
    }

    /**
     * 返回值
     *
     * @param key
     * @return
     */
    public RedisFuture<String> get(String key) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.get(key);
        } else {
            return redisAsyncCommands.get(key);
        }
    }

    /**
     * 设置key值和过期时间
     *
     * @param key
     * @param value
     * @param seconds 秒数，不能小于0
     * @return
     */
    public RedisFuture<String> setByTime(String key, String value, int seconds) {
        if (seconds < 0) {
            return null;
        }
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.setex(key, seconds, value);
        } else {
            return redisAsyncCommands.setex(key, seconds, value);
        }
    }

    /**
     * @param key
     * @param value
     * @param nxxx  NX|XX  是否存在
     *              <li>NX -- Only set the key if it does not already exist.</li>
     *              <li>XX -- Only set the key if it already exist.</li>
     * @param expx  EX|PX, expire time units ，时间单位格式，秒或毫秒
     *              <li>EX = seconds;</li>
     *              <li>PX = milliseconds</li>
     * @param time  expire time in the units of expx，时间（long型），不能小于0
     * @return
     */
    public RedisFuture<String> set(String key, String value,
                                   String nxxx, String expx, long time) {
        if (time < 0) {
            return null;
        }

        try {
            SetArgs setParams = new SetArgs();
            if ("EX".equalsIgnoreCase(expx)) {
                setParams.ex(((Long) time).intValue());
            } else if ("PX".equalsIgnoreCase(expx)) {
                setParams.px(time);
            } else {
                Preconditions.checkArgument("EX".equalsIgnoreCase(expx) || "PX".equalsIgnoreCase(expx));
            }
            if ("NX".equalsIgnoreCase(nxxx)) {
                setParams.nx();
            } else if ("XX".equalsIgnoreCase(nxxx)) {
                setParams.xx();
            } else {
                Preconditions.checkArgument("NX".equalsIgnoreCase(nxxx) || "XX".equalsIgnoreCase(nxxx));
            }
            if (null != clusterAsyncCommands) {
                return clusterAsyncCommands.set(key, value, setParams);
            } else {
                return redisAsyncCommands.set(key, value, setParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置key
     *
     * @param key
     * @param value
     * @param nxxx  NX|XX 是否需要存在
     *              <li>NX -- Only set the key if it does not already exist.</li>
     *              <li>XX -- Only set the key if it already exist.</li>
     * @return
     */
    public RedisFuture<String> set(String key, String value,
                                   String nxxx) {
        try {
            SetArgs setParams = new SetArgs();
            if ("NX".equalsIgnoreCase(nxxx)) {
                setParams.nx();
            } else if ("XX".equalsIgnoreCase(nxxx)) {
                setParams.xx();
            } else {
                Preconditions.checkArgument("NX".equalsIgnoreCase(nxxx) || "XX".equalsIgnoreCase(nxxx));
            }
            if (null != clusterAsyncCommands) {
                return clusterAsyncCommands.set(key, value, setParams);
            } else {
                return redisAsyncCommands.set(key, value, setParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 当key不存在时，设置值，成功返回true
     *
     * @param key
     * @param value
     * @return
     */
    public RedisFuture<Boolean> setIfNotExists(String key, String value) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.setnx(key, value);
        } else {
            return redisAsyncCommands.setnx(key, value);
        }
    }

    /**
     * 当key不存在时，设置值，成功返回true，同setIfNotExists
     *
     * @param key
     * @param value
     * @return
     */
    public RedisFuture<Boolean> setNX(String key, String value) {
        return setIfNotExists(key, value);
    }

    /**
     * 仅当key不存在时则设置值，成功返回true，存在时不设置值
     *
     * @param key
     * @param value
     * @param seconds ，秒数，不能小于0
     * @return
     */
    public RedisFuture<String> setIfNotExists(String key, String value, long seconds) {
        if (seconds < 0) {
            return null;
        }
        return set(key, value, NX, EX, seconds);
    }

    /**
     * 仅当key不存在时则设置值，成功返回true，存在时不设置值，同setIfNotExists(key, value, seconds)
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public RedisFuture<String> setNX(String key, String value, Long seconds) {
        return setIfNotExists(key, value, seconds);
    }

    /**
     * 当key存在时则设置值，成功返回true，不存在不设置值
     *
     * @param key
     * @param value
     * @return
     */
    public RedisFuture<String> setIfExists(String key, String value) {
        return set(key, value, XX);
    }

    /**
     * 当key存在时则设置值，成功返回true，不存在不设置值，同setIfExists
     *
     * @param key
     * @param value
     * @return
     */
    public RedisFuture<String> setXX(String key, String value) {
        return setIfExists(key, value);
    }

    /**
     * 仅当key存在时则设置值，成功返回true，不存在不设置值
     *
     * @param key
     * @param value
     * @param seconds ，秒数，不能小于0
     * @return
     */
    public RedisFuture<String> setIfExists(String key, String value, long seconds) {
        if (seconds < 0) {
            return null;
        }
        return set(key, value, XX, EX, seconds);
    }

    /**
     * 仅当key存在时则设置值，成功返回true，不存在不设置值
     *
     * @param key
     * @param value
     * @param seconds ，秒数，不能小于0
     * @return
     */
    public RedisFuture<String> setXX(String key, String value, long seconds) {
        return setIfExists(key, value, seconds);
    }

    /**
     * 设置超期时间
     *
     * @param key
     * @param seconds 为Null时，将会马上过期。可以设置-1，0，表示马上过期
     * @return
     */
    public RedisFuture<Boolean> setTime(String key, Integer seconds) {
        try {
            if (seconds == null) {
                seconds = -1;
            }
            if (null != clusterAsyncCommands) {
                return clusterAsyncCommands.expire(key, seconds);
            } else {
                return redisAsyncCommands.expire(key, seconds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置超期时间
     *
     * @param key
     * @param seconds 为Null时，将会马上过期。可以设置-1，0，表示马上过期
     * @return
     */
    public RedisFuture<Boolean> setOutTime(String key, Integer seconds) {
        return setTime(key, seconds);
    }

    /**
     * 设置超期时间
     *
     * @param key
     * @param seconds 秒数，为Null时，将会马上过期。可以设置-1，0，表示马上过期
     * @return
     */
    public RedisFuture<Boolean> expire(String key, Integer seconds) {
        return setTime(key, seconds);
    }

    /**
     * 设置超期时间
     *
     * @param key
     * @param value
     * @param seconds 秒数，为Null时，将会马上过期。可以设置-1，0，表示马上过期
     * @return
     */
    public RedisFuture<Boolean> setExpire(String key, String value, Integer seconds) {
        set(key, value);
        return setTime(key, seconds);
    }

    /**
     * 判断key是否存在，存在返回true
     *
     * @param key
     * @return
     */
    public RedisFuture<Boolean> exists(String key) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.exists(key);
        } else {
            return redisAsyncCommands.exists(key);
        }
    }

    /**
     * 判断key是否存在，存在返回true
     *
     * @param key
     * @return
     */
    public RedisFuture<Boolean> isExists(String key) {
        return exists(key);
    }

    /**
     * 将key设置为永久
     *
     * @param key
     * @return
     */
    public RedisFuture<Boolean> persist(String key) {
        try {
            RedisFuture<Long> time = getTime(key);
            Long aLong = time.get();
            if (aLong == -1) {
                return null;
            }

            //已经是永久的，返回0
            if (null != clusterAsyncCommands) {
                return clusterAsyncCommands.persist(key);
            } else {
                return redisAsyncCommands.persist(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取剩余时间（秒）
     *
     * @param key
     * @return
     */
    public RedisFuture<Long> getTime(String key) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.ttl(key);
        } else {
            return redisAsyncCommands.ttl(key);
        }
    }

    /**
     * 获取剩余时间（秒）
     *
     * @param key
     * @return
     */
    public RedisFuture<Long> ttl(String key) {
        return getTime(key);
    }

    /**
     * 随机获取一个key
     *
     * @return
     */
    public RedisFuture<String> randomKey() {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.randomkey();
        } else {
            return redisAsyncCommands.randomkey();
        }
    }

    /**
     * 随机获取一个key
     *
     * @return
     */
    public RedisFuture<String> random() {
        return randomKey();
    }

    /**
     * 修改 key 的名称，成功返回true，如果不存在该key，则会抛错：ERR no such key
     * 注：如果newKey已经存在，则会进行覆盖。建议使用renameNX
     *
     * @param oldkey 原来的key
     * @param newKey 新的key
     * @return
     */
    public RedisFuture<String> rename(String oldkey, String newKey) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.rename(oldkey, newKey);
        } else {
            return redisAsyncCommands.rename(oldkey, newKey);
        }
    }

    /**
     * 仅当 newkey 不存在时，将 key 改名为 newkey 。成功返回true
     *
     * @param oldkey
     * @param newKey
     * @return
     */
    public RedisFuture<Boolean> renameNX(String oldkey, String newKey) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.renamenx(oldkey, newKey);
        } else {
            return redisAsyncCommands.renamenx(oldkey, newKey);
        }
    }

    /**
     * 仅当 newkey 不存在时，将 key 改名为 newkey 。成功返回true
     *
     * @param oldkey
     * @param newKey
     * @return
     */
    public RedisFuture<Boolean> renameIfNotExists(String oldkey, String newKey) {
        return renameNX(oldkey, newKey);
    }

    /**
     * 返回 key 所储存的值的类型。
     *
     * @param key
     * @return
     */
    public RedisFuture<String> type(String key) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.type(key);
        } else {
            return redisAsyncCommands.type(key);
        }
    }

    /**
     * 返回 key 所储存的值的类型。
     *
     * @param key
     * @return
     */
    public RedisFuture<String> getType(String key) {
        return type(key);
    }

    /**
     * 删除key及值
     *
     * @param key
     * @return
     */
    public RedisFuture<Long> del(String key) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.del(key);
        } else {
            return redisAsyncCommands.del(key);
        }
    }

    /**
     * 删除key及值
     *
     * @param key
     * @return
     */
    public RedisFuture<Long> delete(String key) {
        return del(key);
    }

    /**
     * 删除key及值
     *
     * @param key
     * @return
     */
    public RedisFuture<Long> remove(String key) {
        return del(key);
    }

    /**
     * 批量删除key及值
     *
     * @param keys
     * @return
     */
    public RedisFuture<Long> del(String[] keys) {
        if (null != clusterAsyncCommands) {
            return clusterAsyncCommands.del(keys);
        } else {
            return redisAsyncCommands.del(keys);
        }
    }

    /**
     * 批量删除key及值
     *
     * @param keys
     * @return
     */
    public RedisFuture<Long> delete(String[] keys) {
        return del(keys);
    }

    /**
     * 批量删除key及值
     *
     * @param keys
     * @return
     */
    public RedisFuture<Long> remove(String[] keys) {
        return del(keys);
    }

    @Override
    public void close() throws Exception {
        if(null != this.netxFactory) {
            netxFactory.close();
        }
    }
}

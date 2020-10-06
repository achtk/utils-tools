package com.chua.utils.netx.redis.jedis.context;

import com.chua.utils.tools.properties.NetxProperties;
import com.chua.utils.netx.redis.jedis.factory.JedisFactory;
import com.chua.utils.tools.classes.ClassHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.io.*;

/**
 * jedis上下文
 *
 * @author CH
 * @date 2020-09-28
 */
@Slf4j
public class JedisContext implements AutoCloseable {

    private final JedisFactory netxFactory;
    private final ShardedJedisPool shardedJedisPool;
    private NetxProperties netxProperties;

    public JedisContext(@NonNull NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
        this.netxFactory = new JedisFactory(netxProperties);
        this.netxFactory.start();
        this.shardedJedisPool = this.netxFactory.client();
    }

    /**
     * 将对象-->byte[] (由于jedis中不支持直接存储object所以转换成byte[]存入)
     *
     * @param object
     * @return
     */
    private byte[] serialize(Object object) {
        object = serializeObj(object);
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                oos.close();
                baos.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 序列化对象
     * @param obj 对象
     * @return
     */
    private Object serializeObj(final Object obj) {
        if(Serializable.class.isAssignableFrom(obj.getClass())) {
            return obj;
        }
        try {
            return ClassHelper.addInterface(obj, Serializable.class);
        } catch (Throwable throwable) {
            return obj;
        }
    }
    /**
     * 将byte[] -->Object
     *
     * @param bytes
     * @return
     */
    private Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                bais.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    // ------------------------ jedis util ------------------------
    /**
     * 存储简单的字符串或者是Object 因为jedis没有分装直接存储Object的方法，所以在存储对象需斟酌下
     * 存储对象的字段是不是非常多而且是不是每个字段都用到，如果是的话那建议直接存储对象，
     * 否则建议用集合的方式存储，因为redis可以针对集合进行日常的操作很方便而且还可以节省空间
     */

    /**
     * Set String
     *
     * @param key 索引
     * @param value
     * @param seconds 存活时间,单位/秒
     * @return
     */
    public String setStringValue(String key, String value, int seconds) {
        String result = null;
        ShardedJedis client = shardedJedisPool.getResource();
        try {
            result = client.setex(key, seconds, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return result;
    }

    /**
     * Set Object
     *
     * @param key 索引
     * @param obj
     * @param seconds 存活时间,单位/秒
     */
    public String setObjectValue(String key, Object obj, int seconds) {
        String result = null;
        ShardedJedis client = shardedJedisPool.getResource();
        try {
            result = client.setex(key.getBytes(), seconds, serialize(obj));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return result;
    }

    /**
     * Get String
     *
     * @param key 索引
     * @return
     */
    public String getStringValue(String key) {
        String value = null;
        ShardedJedis client = shardedJedisPool.getResource();
        try {
            value = client.get(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return value;
    }

    /**
     * Get Object
     *
     * @param key 索引
     * @return
     */
    public Object getObjectValue(String key) {
        Object obj = null;
        ShardedJedis client = shardedJedisPool.getResource();
        try {
            byte[] bytes = client.get(key.getBytes());
            if (bytes != null && bytes.length > 0) {
                obj = unserialize(bytes);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return obj;
    }

    /**
     * Delete key
     *
     * @param key 索引
     * @return Integer reply, specifically:
     * an integer greater than 0 if one or more keys were removed
     * 0 if none of the specified key existed
     */
    public Long del(String key) {
        Long result = null;
        ShardedJedis client = shardedJedisPool.getResource();
        try {
            result = client.del(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return result;
    }

    /**
     * incrBy i(+i)
     *
     * @param key 索引
     * @param i 序列
     * @return new value after incr
     */
    public Long incrBy(String key, int i) {
        Long result = null;
        ShardedJedis client = shardedJedisPool.getResource();
        try {
            result = client.incrBy(key, i);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return result;
    }

    /**
     * exists valid
     *
     * @param key 索引
     * @return Boolean reply, true if the key exists, otherwise false
     */
    public boolean exists(String key) {
        Boolean result = null;
        ShardedJedis client = shardedJedisPool.getResource();
        try {
            result = client.exists(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return result;
    }

    /**
     * expire reset
     *
     * @param key 索引
     * @param seconds 存活时间,单位/秒
     * @return Integer reply, specifically:
     * 1: the timeout was set.
     * 0: the timeout was not set since the key already has an associated timeout (versions lt 2.1.3), or the key does not exist.
     */
    public long expire(String key, int seconds) {
        Long result = null;
        ShardedJedis client = shardedJedisPool.getResource();
        try {
            result = client.expire(key, seconds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return result;
    }

    /**
     * expire at unixTime
     *
     * @param key 索引
     * @param unixTime 超时时间
     * @return
     */
    public long expireAt(String key, long unixTime) {
        Long result = null;
        ShardedJedis client = shardedJedisPool.getResource();
        try {
            result = client.expireAt(key, unixTime);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        if (null != this.netxFactory) {
            netxFactory.close();
        }
    }
}

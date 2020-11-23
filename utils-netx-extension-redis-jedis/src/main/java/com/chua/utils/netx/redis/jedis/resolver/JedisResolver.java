package com.chua.utils.netx.redis.jedis.resolver;

import com.chua.utils.netx.redis.jedis.util.JedisUtil;
import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.kv.NetKeyValue;
import com.chua.utils.tools.bean.copy.BeanCopy;
import lombok.NoArgsConstructor;
import redis.clients.jedis.JedisPool;

import java.util.Properties;
import java.util.Set;

/**
 * jedis解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@NoArgsConstructor
public class JedisResolver extends NetResolver<JedisPool> implements NetKeyValue<String, String> {

    private JedisPool jedisPool;

    @Override
    public void setProperties(Properties properties) {
        JedisPool jedisPool = new JedisPool();
        BeanCopy<JedisPool> beanCopy = BeanCopy.of(jedisPool);
        beanCopy.with(properties);
        this.jedisPool = beanCopy.create();
        super.setProperties(properties);
    }

    @Override
    public Service<JedisPool> get() {
        return new Service(jedisPool);
    }

    @Override
    public String getValue(String key) {
        return JedisUtil.getValue(key, jedisPool);
    }

    @Override
    public Long del(String key) {
        return JedisUtil.del(key, jedisPool);
    }

    @Override
    public boolean exist(String key) {
        return JedisUtil.exists(key, jedisPool);
    }

    @Override
    public String set(String key, String value) {
        return JedisUtil.set(key, value, jedisPool);
    }

    @Override
    public Set<String> keys(String key) {
        return JedisUtil.keys(key, jedisPool);
    }
}

package com.chua.utils.netx.redis.jedis.resolver;

import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.tools.bean.copy.BeanCopy;
import lombok.NoArgsConstructor;
import redis.clients.jedis.JedisPool;

import java.util.Properties;

/**
 * jedis解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@NoArgsConstructor
public class JedisResolver extends NetResolver {

    private JedisPool jedisPool;

    public JedisResolver(Properties properties) {
        super(properties);
        JedisPool jedisPool = new JedisPool();
        BeanCopy<JedisPool> beanCopy = BeanCopy.of(jedisPool);
        beanCopy.with(properties);
        this.jedisPool = beanCopy.create();
    }

    @Override
    public Service get() {
        return new Service(jedisPool);
    }
}

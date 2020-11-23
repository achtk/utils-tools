package com.chua.utils.netx.resolver;

import com.chua.utils.netx.resolver.entity.Service;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Properties;

/**
 * net解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@NoArgsConstructor
public abstract class NetResolver<T> {

    @Setter
    private Properties properties;

    public NetResolver(Properties properties) {
        this.properties = properties;
        setProperties(properties);
    }

    /**
     * 获取服务
     *
     * @return 服务
     */
    abstract public Service<T> get();
}

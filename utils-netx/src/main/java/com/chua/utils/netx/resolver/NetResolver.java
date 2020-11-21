package com.chua.utils.netx.resolver;

import com.chua.utils.netx.resolver.entity.Service;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Properties;
import java.util.function.Supplier;

/**
 * net解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@NoArgsConstructor
public abstract class NetResolver implements Supplier<Service> {

    @Setter
    private Properties properties;

    public NetResolver(Properties properties) {
        this.properties = properties;
        setProperties(properties);
    }
}

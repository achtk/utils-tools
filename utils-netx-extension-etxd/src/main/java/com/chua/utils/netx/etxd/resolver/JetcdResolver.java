package com.chua.utils.netx.etxd.resolver;

import com.chua.utils.netx.etxd.context.JetcdContext;
import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.tools.properties.NetProperties;

import java.util.Properties;

/**
 * etcd resolver
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public class JetcdResolver extends NetResolver<JetcdContext> {

    private JetcdContext jetcdContext;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.jetcdContext = new JetcdContext(NetProperties.newProperty(properties));
    }

    @Override
    public Service<JetcdContext> get() {
        return new Service<>(jetcdContext);
    }
}

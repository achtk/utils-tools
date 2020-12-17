package com.chua.utils.netx.neo4j;

import com.chua.utils.netx.neo4j.context.Neo4jContext;
import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.tools.properties.NetProperties;

import java.util.Properties;

/**
 * neo4j解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public class Neo4jResolver extends NetResolver<Neo4jContext> {

    private Neo4jContext neo4jContext;

    @Override
    public void setProperties(Properties properties) {
        this.neo4jContext = new Neo4jContext(NetProperties.newProperty(properties));
        super.setProperties(properties);
    }

    @Override
    public Service<Neo4jContext> get() {
        return new Service<>(neo4jContext);
    }
}

package com.chua.utils.netx.nacos.resolver;

import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.NetNodeConf;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.node.NetNode;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * nacos
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class NacosConfigResolver extends NetResolver implements NetNode {
    @Override
    public byte[] addNode(NetNodeConf netNodeConf, byte[] data) throws IOException {
        return new byte[0];
    }

    @Override
    public byte[] deleteNode(NetNodeConf netNodeConf) throws IOException {
        return new byte[0];
    }

    @Override
    public boolean existNode(NetNodeConf netNodeConf) throws IOException {
        return false;
    }

    @Override
    public void monitor(NetNodeConf netNodeConf, Consumer consumer) throws IOException {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Service get() {
        return null;
    }
}

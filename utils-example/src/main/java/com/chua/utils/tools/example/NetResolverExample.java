package com.chua.utils.tools.example;

import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.tools.spi.extension.ExtensionLoader;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import redis.clients.jedis.BinaryJedis;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class NetResolverExample {

    public static void main(String[] args) {
        ExtensionLoader<NetResolver> extensionLoader = ExtensionFactory.getExtensionLoader(NetResolver.class);
        NetResolver randomSpiService = extensionLoader.getRandomSpiService();
        System.out.println();
    }
}

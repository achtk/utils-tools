package com.chua.utils.tools.proxy;

import com.chua.utils.tools.loader.BalancerLoader;
import com.chua.utils.tools.mapper.ProxyMapper;

/**
 * 代理加载器
 * @author CH
 */
public class ProxyLoader<T> {

    private ProxyAgent<T> proxyAgent;

    public ProxyLoader(BalancerLoader balancerLoader, ProxyMapper proxyMapper) {
        this.proxyAgent = new DefaultProxyAgent<T>(proxyMapper, balancerLoader);
    }

    public ProxyLoader(ProxyAgent<T> proxyAgent) {
        this.proxyAgent = proxyAgent;
    }

    /**
     * 产生代理
     * @return
     */
    public T newProxy(Class<T> tClass) {
        return proxyAgent.newProxy(tClass);
    }
}


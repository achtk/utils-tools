package com.chua.utils.tools.example;

import com.chua.utils.tools.encrypt.IEncrypt;
import com.chua.utils.tools.proxy.DefaultProxyAgent;
import com.chua.utils.tools.proxy.ProxyLoader;
import org.testng.annotations.Test;

/**
 * @author CH
 */
public class ProxyLoaderTest {

    @Test
    public void proxy() throws Exception {
        ProxyLoader<IEncrypt> proxyLoader = new ProxyLoader(new DefaultProxyAgent());
        IEncrypt encrypt = proxyLoader.newProxy(IEncrypt.class);
        encrypt.encode("");
    }
}

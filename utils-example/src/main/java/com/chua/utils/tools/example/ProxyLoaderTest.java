package com.chua.utils.tools.example;

import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.proxy.DefaultProxyAgent;
import com.chua.utils.tools.proxy.ProxyLoader;
import org.testng.annotations.Test;

/**
 * @author CH
 */
public class ProxyLoaderTest {

    @Test
    public void proxy() throws Exception {
        ProxyLoader<Encrypt> proxyLoader = new ProxyLoader(new DefaultProxyAgent());
        Encrypt encrypt = proxyLoader.newProxy(Encrypt.class);
        encrypt.encode("");
    }
}

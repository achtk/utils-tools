package com.chua.utils.tools.example;

import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import org.testng.annotations.Test;

/**
 * @author CH
 */
public class NetxFactoryExample {

    @Test
    public void testNetxFactory() {
        INetxFactory netxFactory = ExtensionFactory.getExtensionLoader(INetxFactory.class).getExtension();
        System.out.println(netxFactory);
    }
}

package com.chua.utils.tools.example;

import com.chua.utils.netx.factory.INetFactory;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import org.testng.annotations.Test;

/**
 * @author CH
 */
public class NetxFactoryTest {

    @Test
    public void testNetxFactory() {
        INetFactory netxFactory = ExtensionFactory.getExtensionLoader(INetFactory.class).getExtension();
        System.out.println(netxFactory);
    }
}

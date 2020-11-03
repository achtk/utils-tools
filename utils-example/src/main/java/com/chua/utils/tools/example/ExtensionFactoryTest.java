package com.chua.utils.tools.example;

import com.chua.utils.tools.encrypt.Encrypt;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import org.testng.annotations.Test;

/**
 * spi工厂
 * @author CH
 */
public class ExtensionFactoryTest {
    /**
     * 获取base64名称  IEncrypt子类
     * @
     */
    @Test
    public void testGetExtensionLoader() throws Exception {
        Encrypt base64 = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("base64");
        System.out.println(base64.toHexString("test"));
        System.out.println(base64.fromHexString(base64.toHexString("test")));

        Encrypt des = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("des");
        des.append("encrypt-key", "2323232");
        System.out.println(des.toHexString("test"));
        System.out.println(des.fromHexString(des.toHexString("test")));


        Encrypt aes = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("aes");
        aes.append("encrypt-key", "2323232");
        System.out.println(aes.toHexString("test"));
        System.out.println(aes.fromHexString(aes.toHexString("test")));

    }
}
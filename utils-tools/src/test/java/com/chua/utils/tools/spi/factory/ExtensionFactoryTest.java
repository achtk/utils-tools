package com.chua.utils.tools.spi.factory;

import com.chua.utils.tools.encrypt.IEncrypt;
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
        IEncrypt base64 = ExtensionFactory.getExtensionLoader(IEncrypt.class).getExtension("base64");
        System.out.println(base64.encodeForString("test"));
        System.out.println(base64.decodeForString(base64.encodeForString("test")));

        IEncrypt des = ExtensionFactory.getExtensionLoader(IEncrypt.class).getExtension("des");
        des.append("encrypt-key", "2323232");
        System.out.println(des.encodeFromString("test"));
        System.out.println(des.decodeForString(des.encodeFromString("test")));


        IEncrypt aes = ExtensionFactory.getExtensionLoader(IEncrypt.class).getExtension("aes");
        aes.append("encrypt-key", "2323232");
        System.out.println(aes.encodeFromString("test"));
        System.out.println(aes.decodeForString(aes.encodeFromString("test")));

    }
}
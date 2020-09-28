package com.chua.utils.tools.example;

import com.chua.utils.tools.encrypt.IEncrypt;
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
        IEncrypt base64 = ExtensionFactory.getExtensionLoader(IEncrypt.class).getExtension("base64");
        System.out.println(base64.toHexString("test"));
        System.out.println(base64.fromHexString(base64.toHexString("test")));

        IEncrypt des = ExtensionFactory.getExtensionLoader(IEncrypt.class).getExtension("des");
        des.append("encrypt-key", "2323232");
        System.out.println(des.toHexString("test"));
        System.out.println(des.fromHexString(des.toHexString("test")));


        IEncrypt aes = ExtensionFactory.getExtensionLoader(IEncrypt.class).getExtension("aes");
        aes.append("encrypt-key", "2323232");
        System.out.println(aes.toHexString("test"));
        System.out.println(aes.fromHexString(aes.toHexString("test")));

    }
}
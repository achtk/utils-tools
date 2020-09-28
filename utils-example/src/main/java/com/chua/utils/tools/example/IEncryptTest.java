package com.chua.utils.tools.example;

import com.chua.utils.tools.encrypt.IEncrypt;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.google.common.base.Charsets;
import org.testng.annotations.Test;

/**
 * @author CH
 * @date 2020-09-28
 */
public class IEncryptTest {


    @Test
    public void test() throws Exception {
        IEncrypt base64 = ExtensionFactory.getExtensionLoader(IEncrypt.class).getExtension("base64");
        System.out.println("base64: "+ base64.toHexString("test"));
        System.out.println("base64: "+ base64.fromHexToString(base64.toHexString("test")));

        IEncrypt des = ExtensionFactory.getExtensionLoader(IEncrypt.class).getExtension("des");
        des.append("encrypt-key", "2323232");
        System.out.println("des: "+ des.toHexString("test"));
        System.out.println("des: "+ des.fromHexToString(des.toHexString("test")));

        IEncrypt aes = ExtensionFactory.getExtensionLoader(IEncrypt.class).getExtension("aes");
        aes.append("encrypt-key", "2323232");
        System.out.println("aes: "+ aes.toHexString("test"));
        System.out.println("aes: "+ aes.fromHexToString(aes.toHexString("test")));

        IEncrypt desede = ExtensionFactory.getExtensionLoader(IEncrypt.class).getExtension("desede");
        desede.append("encrypt-key", "2323232");
        System.out.println("desede: "+ desede.toHexString("test"));
        System.out.println("desede: "+ desede.fromHexToString(desede.toHexString("test")));
    }

}
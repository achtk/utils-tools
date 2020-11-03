package com.chua.utils.tools.example;

import com.chua.utils.tools.encrypt.Encrypt;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import org.testng.annotations.Test;

/**
 * @author CH
 * @date 2020-09-28
 */
public class EncryptTest {


    @Test
    public void test() throws Exception {
        Encrypt base64 = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("base64");
        System.out.println("base64: "+ base64.toHexString("test"));
        System.out.println("base64: "+ base64.fromHexToString(base64.toHexString("test")));

        System.out.println("============================================================");
        Encrypt des = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("des");
        des.append("encrypt-key", "2323232");
        System.out.println("des: "+ des.toHexString("test"));
        System.out.println("des: "+ des.fromHexToString(des.toHexString("test")));

        System.out.println("============================================================");
        Encrypt aes = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("aes");
        aes.append("encrypt-key", "2323232");
        System.out.println("aes: "+ aes.toHexString("test"));
        System.out.println("aes: "+ aes.fromHexToString(aes.toHexString("test")));

        System.out.println("============================================================");
        Encrypt desede = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("desede");
        desede.append("encrypt-key", "2323232");
        System.out.println("desede: "+ desede.toHexString("test"));
        System.out.println("desede: "+ desede.fromHexToString(desede.toHexString("test")));
        System.out.println("============================================================");
        Encrypt md5 = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("md5");
        md5.append("encrypt-key", "2323232");
        System.out.println("desede: "+ md5.toHexString("test"));
        System.out.println("desede: "+ md5.fromHexToString(md5.toHexString("test")));
    }

}
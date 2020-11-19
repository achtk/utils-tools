package com.chua.utils.tools.example;

import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.testng.annotations.Test;

/**
 * @author CH
 * @date 2020-09-28
 */
public class EncryptExample {

    private static final String SOURCE = "test";

    public static void main(String[] args) throws Exception {
        //测试base64
        testBase64();
        //测试des
        testDes();
        //测试 aes
        testAes();
        //测试 desede
        testDesede();
        //测试 md5
        testMd5();
    }


    private static void testDesede() throws Exception {
        System.out.println("============================================================");
        Encrypt des = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("desede");
        //添加秘钥
        des.append("encrypt-key", "2323232");
        String encode = des.encode(SOURCE);

        System.out.println("desede: " + encode);
        System.out.println("desede: " + des.decode(encode));
    }

    private static void testAes() throws Exception {
        System.out.println("============================================================");
        Encrypt des = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("aes");
        //添加秘钥
        des.append("encrypt-key", "2323232");
        String encode = des.encode(SOURCE);

        System.out.println("aes: " + encode);
        System.out.println("aes: " + des.decode(encode));
    }

    private static void testDes() throws Exception {
        System.out.println("============================================================");
        Encrypt des = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("des");
        //添加秘钥
        des.append("encrypt-key", "2323232");
        String encode = des.encode(SOURCE);

        System.out.println("des: " + encode);
        System.out.println("des: " + des.decode(encode));
    }

    private static void testBase64() throws Exception {
        Encrypt encrypt = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("base64");
        String encode = encrypt.encode(SOURCE);

        System.out.println("base64: " + encode);
        System.out.println("base64: " + encrypt.decode(encode));
    }

    private static void testMd5() throws Exception {

        System.out.println("============================================================");
        Encrypt md5 = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension("md5");
        System.out.println("md5: " + md5.encode(SOURCE));
        System.out.println("md5: " + md5.decode(md5.encode(SOURCE)));
    }

}
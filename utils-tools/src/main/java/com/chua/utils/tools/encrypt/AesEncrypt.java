package com.chua.utils.tools.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Properties;

/**
 * aes加解密
 *
 * @author CH
 */
public class AesEncrypt implements IEncrypt {
    private Properties properties;
    private static final String AES = "AES";

    @Override
    public byte[] encode(byte[] bytes) {
        try {
            // 创建AES的Key生产者
            KeyGenerator kgen = KeyGenerator.getInstance(AES);
            // 利用用户密码作为随机数初始化出
            kgen.init(128, new SecureRandom(getKey(16)));
            // 128位的key生产者
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
            // 根据用户密码，生成一个密钥
            SecretKey secretKey = kgen.generateKey();
            // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
            byte[] enCodeFormat = secretKey.getEncoded();
            // null。

            // 转换为AES专用密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(AES);

            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果有错就返加nulll
        return null;
    }

    @Override
    public byte[] decode(byte[] bytes) throws Exception {
        //1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keygen = KeyGenerator.getInstance(AES);
        //2.根据ecnodeRules规则初始化密钥生成器
        //生成一个128位的随机源,根据传入的字节数组
        keygen.init(128, new SecureRandom(getKey(16)));
        //3.产生原始对称密钥
        SecretKey original_key = keygen.generateKey();
        //4.获得原始对称密钥的字节数组
        byte[] raw = original_key.getEncoded();
        //5.根据字节数组生成AES密钥
        SecretKey key = new SecretKeySpec(raw, AES);
        //6.根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance(AES);
        //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.DECRYPT_MODE, key);
        /*
         * 解密
         */
        return cipher.doFinal(bytes);
    }
}

package com.chua.utils.tools.encrypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;

/**
 * aes加解密
 *
 * @author CH
 */
public class AesEncrypt implements IEncrypt {
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private Properties properties;
    private static final String AES = "AES";

    @Override
    public byte[] encode(byte[] bytes) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        //使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(16), AES));
        //执行操作
        return cipher.doFinal(bytes);
    }

    @Override
    public byte[] decode(byte[] bytes) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        //使用密钥初始化，设置为加密模式
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey(16), AES));
        //执行操作
        return cipher.doFinal(bytes);
    }
}

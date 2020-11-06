package com.chua.utils.tools.common.codec.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;

/**
 * DESede加解密
 *
 * @author CH
 * @date 2020-09-28
 */
public class DesedeEncrypt extends AbstractStandardEncrypt {
    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "DESede";

    private static final int LENGTH = 512;

    private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/ISO10126Padding";

    @Override
    public byte[] encode(byte[] bytes) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        //使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, toKey(getKey(LENGTH)));
        //执行操作
        return cipher.doFinal(bytes);
    }

    @Override
    public byte[] decode(byte[] bytes) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, toKey(getKey(LENGTH)));
        //执行操作
        return cipher.doFinal(bytes);
    }

    /**
     * 初始化密钥
     *
     * @return byte[] 密钥
     * @throws Exception
     */
    public static byte[] initSecretKey() throws Exception {
        //返回生成指定算法的秘密密钥的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        //初始化此密钥生成器，使其具有确定的密钥大小
        kg.init(168);
        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return Key  密钥
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        byte[] initSecretKey = initSecretKey();
        //实例化DES密钥规则
        DESedeKeySpec dks = new DESedeKeySpec(key);
        //实例化密钥工厂
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        //生成密钥
        SecretKey secretKey = skf.generateSecret(dks);
        return secretKey;
    }
}

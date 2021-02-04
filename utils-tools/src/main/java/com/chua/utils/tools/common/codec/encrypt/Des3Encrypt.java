package com.chua.utils.tools.common.codec.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * 3des加解密
 *
 * @author CH
 */
public class Des3Encrypt extends AbstractStandardEncrypt {
    private static final String DES = "DESede";
    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

    @Override
    public byte[] encode(byte[] bytes) {
        try {
            //加密
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key(secretKey()));
            return cipher.doFinal(bytes);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decode(byte[] bytes) throws Exception {
        //加密
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        //解密
        cipher.init(Cipher.DECRYPT_MODE, key(secretKey()));
        return cipher.doFinal(bytes);
    }

    /**
     * 生成KEY
     *
     * @return SecretKey
     */
    public SecretKey secretKey() throws NoSuchAlgorithmException {
        //生成KEY
        KeyGenerator keyGenerator = KeyGenerator.getInstance(DES);
        keyGenerator.init(new SecureRandom());
        return keyGenerator.generateKey();
    }

    /**
     * KEY转换
     *
     * @return SecretKey
     */
    public Key key(SecretKey secretKey) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        //KEY转换
        DESKeySpec desKeySpec = new DESKeySpec(secretKey.getEncoded());
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        return factory.generateSecret(desKeySpec);
    }
}

package com.chua.utils.tools.common.codec.encrypt;

import com.chua.utils.tools.common.codec.BinaryDecoder;
import com.chua.utils.tools.common.codec.BinaryEncoder;
import com.chua.utils.tools.common.codec.StringDecoder;
import com.chua.utils.tools.common.codec.StringEncoder;
import com.chua.utils.tools.environment.Environment;
import com.chua.utils.tools.prop.source.PropertySource;
import com.chua.utils.tools.spi.Spi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加解密
 *
 * @author CH
 */
@Spi("md5")
public interface Encrypt extends Environment, BinaryEncoder, StringEncoder, BinaryDecoder, StringDecoder {

    /**
     * 获取 MessageDigest
     *
     * @param algorithm
     * @return
     */
    default MessageDigest messageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 追加数据
     *
     * @param key   索引
     * @param value 值
     */
    default void append(String key, Object value) {
        PropertySource propertySource = new PropertySource();
        propertySource.setName("encrypt");
        propertySource.add(key, value);
    }
    /**
     * 追加秘钥
     *
     * @param value 值
     */
    default void appendSecret(Object value) {
        PropertySource propertySource = new PropertySource();
        propertySource.setName("encrypt-key");
        propertySource.add(AbstractStandardEncrypt.ENCRYPT_KEY, value);
    }

}

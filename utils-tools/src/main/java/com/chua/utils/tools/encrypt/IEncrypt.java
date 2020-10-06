package com.chua.utils.tools.encrypt;

import com.chua.utils.tools.common.ByteHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.function.producer.IEnvironmentProducer;
import com.chua.utils.tools.spi.Spi;
import com.google.common.base.Charsets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * 加解密
 * @author CH
 */
@Spi("md5")
public interface IEncrypt extends IEnvironmentProducer {

    /**
     *
     */
    public static final byte DEFAULT = 0x7e;
    /**
     * key
     */
    public static final String ENCRYPT_KEY = "encrypt";
    /**
     * 默认key
     */
    public static final String DEFAULT_ENCRYPT_KEY_VALUE = "1234567890";

    /**
     * 环境变量
     */
    public static final Properties PROPERTIES = new Properties();

    /**
     * 加密
     *
     * @param bytes  加密字符
     * @throws Exception
     * @return byte[]
     */
    public byte[] encode(final byte[] bytes) throws Exception;
    /**
     * 加密
     *
     * @param bytes  解密字符
     * @throws Exception
     * @return byte[]
     */
    default public String toHexString(final byte[] bytes) throws Exception {
        byte[] encode = encode(bytes);
        return null == encode ? null : StringHelper.toHexString(encode);
    }
    /**
     * 加密
     *
     * @param bytes  解密字符
     * @throws Exception
     * @return byte[]
     */
    default public String toString(final byte[] bytes) throws Exception {
        byte[] encode = encode(bytes);
        return null == encode ? null : StringHelper.toString(encode, Charsets.UTF_8);
    }
    /**
     * 加密
     *
     * @param source  解密字符
     * @throws Exception
     * @return byte[]
     */
    default public String toHexString(final String source) throws Exception {
        byte[] encode = encode(source.getBytes(Charsets.UTF_8));
        return null == encode ? null : StringHelper.toHexString(encode);
    }
    /**
     * 加密
     *
     * @param source  解密字符
     * @throws Exception
     * @return byte[]
     */
    default public String toString(final String source) throws Exception {
        byte[] encode = encode(source);
        return null == encode ? null : StringHelper.toString(encode, Charsets.UTF_8);
    }
    /**
     * 加密
     *
     * @param source  解密字符
     * @throws  Exception
     * @return byte[]
     */
    default byte[] encode(final String source) throws Exception {
        return encode(source.getBytes(Charsets.UTF_8));
    }
    /**
     * 解密
     *
     * @param bytes  加密字符
     * @throws Exception
     * @return byte[]
     */
    public byte[] decode(final byte[] bytes) throws Exception;

    /**
     * 解密
     *
     * @param bytes  加密字符
     * @throws Exception
     * @return byte[]
     */
    default public String fromHexString(final byte[] bytes) throws Exception {
        byte[] decode = decode(bytes);
        return null == decode ? null : StringHelper.toHexString(decode);
    }
    /**
     * 解密
     *
     * @param bytes  加密字符
     * @throws Exception
     * @return byte[]
     */
    default public String fromString(final byte[] bytes) throws Exception {
        byte[] decode = decode(bytes);
        return null == decode ? null : StringHelper.toString(decode, Charsets.UTF_8);
    }
    /**
     * 解密
     *
     * @param source  加密字符
     * @throws Exception
     * @return byte[]
     */
    default public String fromHexString(final String source) throws Exception {
        byte[] decode = decode(StringHelper.fromHexString(source));
        return null == decode ? null : StringHelper.toHexString(decode);
    }
    /**
     * 解密
     *
     * @param source  加密字符
     * @throws Exception
     * @return byte[]
     */
    default public String fromHexToString(final String source) throws Exception {
        byte[] decode = decode(StringHelper.fromHexString(source));
        return null == decode ? null : StringHelper.toString(decode, Charsets.UTF_8);
    }
    /**
     * 解密
     *
     * @param source  加密字符
     * @throws Exception
     * @return byte[]
     */
    default public String fromString(final String source) throws Exception {
        byte[] decode = decode(ByteHelper.getBytes(source, Charsets.UTF_8));
        return null == decode ? null : StringHelper.toString(decode, Charsets.UTF_8);
    }
    /**
     * 解密
     *
     * @param source  加密字符
     * @throws Exception
     * @return byte[]
     */
    default public byte[] toHexByte(final String source) throws Exception {
        return decode(StringHelper.fromHexString(source));
    }
    /**
     * 获取 MessageDigest
     * @param algorithm
     * @return
     */
    default public MessageDigest messageDigest(String algorithm) {
        try {
           return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    @Override
    default public Properties getEnvironment() {
        return PROPERTIES;
    }

    @Override
    default public void setEnvironment(Properties properties) {
        PROPERTIES.putAll(properties);
    }

    /**
     * 获取索引
     * @return
     */
    default public byte[] getKey(final int size) {
        if(null == getEnvironment()) {
            return null;
        }
        byte[] bytes = getEnvironment().getProperty(ENCRYPT_KEY, DEFAULT_ENCRYPT_KEY_VALUE).getBytes(Charsets.UTF_8);
        if(size == -1) {
            return bytes;
        }
        if(bytes.length < size) {
            byte[] bytes1 = new byte[size];
            System.arraycopy(bytes, 0, bytes1, 0, bytes.length);
            return bytes1;
        }
        return bytes;
    }
}

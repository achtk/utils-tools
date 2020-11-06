package com.chua.utils.tools.common.codec.digest;


import com.chua.utils.tools.common.charset.CharsetHelper;
import com.chua.utils.tools.common.codec.algorithm.Algorithm;
import com.chua.utils.tools.common.codec.binary.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 算法加解密
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/4
 */
public final class DigestHelper {

    private static final int STREAM_BUFFER_LENGTH = 1024;

    /**
     * 获取SHA-512 数据
     *
     * @param data 数据
     * @return byte[]
     */
    public static byte[] sha512(final byte[] data) {
        return getSha512Digest().digest(data);
    }

    /**
     * 获取SHA-512 数据
     *
     * @param data 数据
     * @return byte[]
     * @throws IOException IOException
     */
    public static byte[] sha512(final InputStream data) throws IOException {
        return digest(getSha512Digest(), data);
    }

    /**
     * 获取SHA-512 数据
     *
     * @param data 数据
     * @return byte[]
     * @throws IOException IOException
     */
    public static byte[] sha512(final String data) {
        return sha512(CharsetHelper.getBytesUtf8(data));
    }

    /**
     * 获取SHA-512 数据
     *
     * @param data 数据
     * @return byte[]
     * @throws IOException IOException
     */
    public static byte[] sha256(final InputStream data) throws IOException {
        return digest(getSha256Digest(), data);
    }

    /**
     * 获取SHA-512 HEX数据
     *
     * @param data 数据
     * @return byte[]
     */
    public static String sha512Hex(final byte[] data) {
        return Hex.encodeHexString(sha512(data));
    }

    /**
     * 获取SHA-512 HEX数据
     *
     * @param data 数据
     * @return byte[]
     * @throws IOException IOException
     */
    public static String sha512Hex(final InputStream data) throws IOException {
        return Hex.encodeHexString(sha512(data));
    }

    /**
     * 获取SHA-512 HEX数据
     *
     * @param data 数据
     * @return byte[]
     */
    public static String sha512Hex(final String data) {
        return Hex.encodeHexString(sha512(data));
    }

    /**
     * 获取SHA-256数据
     *
     * @param data 数据
     * @return byte[]
     */
    public static byte[] sha256(final String data) {
        return sha256(CharsetHelper.getBytesUtf8(data));
    }

    /**
     * 获取SHA-256数据
     *
     * @param data 数据
     * @return byte[]
     */
    public static byte[] sha256(final byte[] data) {
        return getSha256Digest().digest(data);
    }

    /**
     * 获取SHA-256 HEX数据
     *
     * @param data 数据
     * @return byte[]
     */
    public static String sha256Hex(final byte[] data) {
        return Hex.encodeHexString(sha256(data));
    }

    /**
     * 获取SHA-256 HEX数据
     *
     * @param data 数据
     * @return byte[]
     * @throws IOException IOException
     */
    public static String sha256Hex(final InputStream data) throws IOException {
        return Hex.encodeHexString(sha256(data));
    }

    /**
     * 获取SHA-256 HEX数据
     *
     * @param data 数据
     * @return byte[]
     */
    public static String sha256Hex(final String data) {
        return Hex.encodeHexString(sha256(data));
    }

    /**
     * 计算MD5摘要，并以32个字符的十六进制字符串形式返回值。
     *
     * @param data 算法数据
     * @return MD5 摘要为十六进制字符串
     */
    public static String md5Hex(final byte[] data) {
        return Hex.encodeHexString(md5(data));
    }

    /**
     * 计算MD5摘要，并以32个字符的十六进制字符串形式返回值。
     *
     * @param data 算法数据
     * @return MD5 摘要为十六进制字符串
     */
    public static String md5Hex(final InputStream data) throws IOException {
        return Hex.encodeHexString(md5(data));
    }

    /**
     * 计算MD5摘要，并以32个字符的十六进制字符串形式返回值。
     *
     * @param data 算法数据
     * @return MD5 摘要为十六进制字符串
     */
    public static String md5Hex(final String data) {
        return Hex.encodeHexString(md5(data));
    }

    /**
     * MD5加密
     *
     * @param data 原数据
     * @return byte[]
     */
    public static byte[] md5(final String data) {
        return md5(CharsetHelper.getBytesUtf8(data));
    }

    /**
     * MD5加密
     *
     * @param data 原数据
     * @return byte[]
     */
    public static byte[] md5(final InputStream data) throws IOException {
        return digest(getMd5Digest(), data);
    }

    /**
     * md5加密
     *
     * @param data 数据
     * @return byte[]
     */
    public static byte[] md5(final byte[] data) {
        return getMd5Digest().digest(data);
    }

    /**
     * 获取加密算法
     *
     * @return MessageDigest
     */
    public static MessageDigest getMd5Digest() {
        return getDigest(toAlgorithm(Algorithm.MD5));
    }

    /**
     * 获取加密算法
     *
     * @return MessageDigest
     */
    public static MessageDigest getSha256Digest() {
        return getDigest(toAlgorithm(Algorithm.SHA_256));
    }

    /**
     * 获取加密算法
     *
     * @return MessageDigest
     */
    public static MessageDigest getSha512Digest() {
        return getDigest(toAlgorithm(Algorithm.SHA_512));
    }

    /**
     * 获取jdk识别的算法
     *
     * @param algorithm 算法
     * @return 算法名称
     */
    private static String toAlgorithm(Algorithm algorithm) {
        return algorithm.name().replace("_", "-").toUpperCase();
    }

    /**
     * 获取加密算法
     *
     * @param algorithm 演算法
     * @return MessageDigest
     */
    public static MessageDigest getDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 获取加密算法数据
     *
     * @param messageDigest 算法
     * @param data          数据
     * @return MessageDigest
     */
    public static byte[] digest(final MessageDigest messageDigest, final InputStream data) throws IOException {
        return updateDigest(messageDigest, data).digest();
    }

    /**
     * 更新算法数据
     *
     * @param digest      算法
     * @param inputStream 数据
     * @return MessageDigest
     * @throws IOException IOException
     */
    public static MessageDigest updateDigest(final MessageDigest digest, final InputStream inputStream) throws IOException {
        final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = inputStream.read(buffer, 0, STREAM_BUFFER_LENGTH);

        while (read > -1) {
            digest.update(buffer, 0, read);
            read = inputStream.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }

        return digest;
    }
}

package com.chua.utils.tools.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * byte 工具类
 *
 * @author CH
 */
public class ByteHelper {
    /**
     * 字符串转为字节数组
     *
     * @param source 字符串
     * @return 字节数组
     */
    public static byte[] getBytes(String source) {
        return getBytes(source, StandardCharsets.UTF_8);
    }

    /**
     * 字符串转为字节数组
     *
     * @param source  字符串
     * @param charset 编码
     * @return 字节数组
     */
    public static byte[] getBytes(String source, Charset charset) {
        return source.getBytes(charset);
    }

    /**
     * 字节数组转为字符串
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String toString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 字节数组转为字符串
     *
     * @param bytes   字节数组
     * @param charset 编码
     * @return 字符串
     */
    public static String toString(byte[] bytes, Charset charset) {
        return new String(bytes, charset);
    }
}

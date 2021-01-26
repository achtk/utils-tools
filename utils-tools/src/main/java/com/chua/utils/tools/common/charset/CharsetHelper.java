package com.chua.utils.tools.common.charset;

import com.chua.utils.tools.common.FileHelper;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.Charset.defaultCharset;

/**
 * charset工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/4
 */
public class CharsetHelper {

    /**
     * 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块
     */
    public static final String US_ASCII = "US-ASCII";

    /**
     * ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1
     */
    public static final String ISO_8859_1 = "ISO-8859-1";

    /**
     * 8 位 UCS 转换格式
     */
    public static final String UTF_8 = "UTF-8";

    /**
     * 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序
     */
    public static final String UTF_16BE = "UTF-16BE";

    /**
     * 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序
     */
    public static final String UTF_16LE = "UTF-16LE";

    /**
     * 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识
     */
    public static final String UTF_16 = "UTF-16";

    /**
     * 中文超大字符集
     */
    public static final String GBK = "GBK";

    /**
     * 最常见的中文字符集
     */
    public static final String GB2312 = "GB2312";

    /**
     * 系统字符集编码，如果是Windows，则默认为GBK编码，否则取 {@link Charset#defaultCharset()}
     *
     * @return 系统字符集编码
     * @see {@link Charset#defaultCharset()}
     * @since 3.1.2
     */
    public static Charset systemCharset() {
        return FileHelper.isWindows() ? Charset.forName(GBK) : defaultCharset();
    }

    /**
     * 使用UTF-8字符集将给定的字符串编码为字节序列，并将结果存储到新的ByteBuffer。
     *
     * @param string 字符串
     * @return 编码的字节，如果输入字符串为{@code null}，则为{@code null}
     * @throws NullPointerException NullPointerException
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     */
    public static ByteBuffer getByteBufferUtf8(final String string) {
        return getByteBuffer(string, StandardCharsets.UTF_8);
    }

    /**
     * 使用UTF-8字符集将给定的字符串编码为字节序列，并将结果存储到新的byte 数组中。
     *
     * @param string 字符串
     * @return 编码的字节，如果输入字符串为{@code null}，则为{@code null}
     * @throws NullPointerException NullPointerException
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     */
    public static byte[] getBytesUtf8(final String string) {
        return getBytes(string, StandardCharsets.UTF_8);
    }

    /**
     * 获取Bytes
     *
     * @param string  字符串
     * @param charset 编码
     * @return byte[]
     */
    private static byte[] getBytes(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }

    /**
     * 获取Bytes
     *
     * @param string  字符串
     * @param charset 编码
     * @return ByteBuffer
     */
    private static ByteBuffer getByteBuffer(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return ByteBuffer.wrap(string.getBytes(charset));
    }

}

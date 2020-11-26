package com.chua.utils.tools.common;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.chua.utils.tools.constant.NumberConstant.EIGHTH;

/**
 * byte 工具类
 *
 * @author CH
 */
public class ByteHelper {
    /**
     * byte array转char
     *
     * @param bytes 字节数组
     * @return char char
     */
    public static char bytesToChar(byte[] bytes) {
        return (char) (((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF));
    }

    /**
     * byte array转char array
     *
     * @param bytes 字节数组
     * @return char char array
     */
    public static char[] bytesToChars(byte[] bytes) {
        return ByteBuffer.wrap(bytes).asCharBuffer().array();
    }

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

    /**
     * 字节数组转为字符串
     *
     * @param bytes   字节数组
     * @param charset 编码
     * @return 字符串
     */
    public static String toString(byte[] bytes, String charset) {
        return new String(bytes, Charset.forName(charset));
    }


    /**
     * The left is Bit7, the right is Bit0
     *
     * @param b        the byte
     * @param position 7-0 from left to right
     * @param value    right or left
     * @return the byte
     */
    public static byte setBit(byte b, int position, boolean value) {
        int op = 1 << position;
        int temp;
        if (value) {
            temp = b | op;
        } else {
            op = ~op;
            temp = b & op;
        }
        return (byte) temp;
    }

    /**
     * The left is Bit7, the right is Bit0
     *
     * @param b        the byte
     * @param position 7-0 from left to right
     * @return boolean
     */
    public static boolean getBit(byte b, int position) {
        String s = toBinaryString(b);
        char c = s.charAt(7 - position);
        return c == '1';
    }

    /**
     * 获取字节字符串
     *
     * @param b the byte
     * @return 字符串
     */
    public static String toBinaryString(byte b) {
        StringBuilder s = new StringBuilder(Integer.toBinaryString(b & 0xFF));
        int len = s.length();
        if (len < EIGHTH) {
            int offset = 8 - len;
            for (int j = 0; j < offset; j++) {
                s.insert(0, "0");
            }
        }
        return s.toString();
    }

    /**
     * 获取hex
     *
     * @param b the byte
     * @return String
     */
    public static String toHexString(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        int len = s.length();
        if (len == 1) {
            s = "0" + s;
        }
        return s.toUpperCase();
    }

    /**
     * 解析字符串二进制值
     *
     * @param str 字符串
     * @return the byte
     */
    public static byte parseBinaryString(String str) {
        return (byte) Integer.parseInt(str, 2);
    }

    /**
     * 解析 hex 为 byte
     *
     * @param hex the hex
     * @return the byte
     */
    public static byte parseHexString(String hex) {
        return (byte) Integer.parseInt(hex, 16);
    }

    /**
     * xor
     *
     * @param bytes the byte array
     * @return byte
     */
    public static byte xor(byte... bytes) {
        byte result = 0x00;
        for (byte b : bytes) {
            result ^= b;
        }
        return result;
    }

    /**
     * xor
     *
     * @param bytes the byte array
     * @param begin 开始位置
     * @param end   结束位置
     * @return byte
     */
    public static byte xor(byte[] bytes, int begin, int end) {
        byte result = 0x00;
        for (int i = begin; i < end; i++) {
            result ^= bytes[i];
        }
        return result;
    }

    /**
     * sum
     *
     * @param bytes the byte array
     * @return byte
     */
    public static byte sum(byte... bytes) {
        byte result = 0x00;
        for (byte b : bytes) {
            result += b;
        }
        return result;
    }

    /**
     * sum
     *
     * @param bytes the byte array
     * @param begin 开始位置
     * @param end   结束位置
     * @return byte
     */
    public static byte sum(byte[] bytes, int begin, int end) {
        byte result = 0x00;
        for (int i = begin; i < end; i++) {
            result += bytes[i];
        }
        return result;
    }

    /**
     * int to byte array
     *
     * @param source the int value
     * @return the byte array
     */
    public static byte[] fromInt(int source) {
        byte[] result = new byte[4];
        result[0] = (byte) ((source >> 24) & 0xFF);
        result[1] = (byte) ((source >> 16) & 0xFF);
        result[2] = (byte) ((source >> 8) & 0xFF);
        result[3] = (byte) (source & 0xFF);
        return result;
    }

    /**
     * byte array to int value
     *
     * @param bytes byte array
     * @return int value
     */
    public static int toInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * short value to byte array
     *
     * @param source short value
     * @return byte array
     */
    public static byte[] fromShort(short source) {
        byte[] result = new byte[2];
        result[0] = (byte) ((source >> 8) & 0xFF);
        result[1] = (byte) (source & 0xFF);
        return result;
    }

    /**
     * byte array to short value
     *
     * @param bytes byte array
     * @return short value
     */
    public static short toShort(byte[] bytes) {
        short value = 0;
        for (int i = 0; i < 2; i++) {
            int shift = (2 - 1 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * byte array to unsigned short value
     *
     * @param bytes byte array
     * @return int value
     */
    public static int toUnsignedShort(byte[] bytes) {
        return toShort(bytes) & 0xFFFF;
    }

    /**
     * long value to byte array
     *
     * @param source short value
     * @return byte array
     */
    public static byte[] fromLong(long source) {
        byte[] result = new byte[8];
        result[0] = (byte) ((source >> 56) & 0xFF);
        result[1] = (byte) ((source >> 48) & 0xFF);
        result[2] = (byte) ((source >> 40) & 0xFF);
        result[3] = (byte) ((source >> 32) & 0xFF);
        result[4] = (byte) ((source >> 24) & 0xFF);
        result[5] = (byte) ((source >> 16) & 0xFF);
        result[6] = (byte) ((source >> 8) & 0xFF);
        result[7] = (byte) (source & 0xFF);
        return result;
    }

    /**
     * byte array to long value
     *
     * @param bytes byte array
     * @return long value
     */
    public static long toLong(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            int shift = (8 - 1 - i) * 8;
            value += ((long) (bytes[i] & 0xFF)) << shift;
        }
        return value;
    }

    /**
     * float value to byte array
     *
     * @param source float value
     * @return byte array
     */
    public static byte[] fromFloat(float source) {
        int i = Float.floatToIntBits(source);
        return fromInt(i);
    }

    /**
     * byte array to float value
     *
     * @param bytes byte array
     * @return float value
     */
    public static float toFloat(byte[] bytes) {
        int i = toInt(bytes);
        return Float.intBitsToFloat(i);
    }

    /**
     * double value to byte array
     *
     * @param source double value
     * @return byte array
     */
    public static byte[] fromDouble(double source) {
        long l = Double.doubleToRawLongBits(source);
        return fromLong(l);
    }

    /**
     * byte array to double value
     *
     * @param bytes byte array
     * @return double value
     */
    public static double toDouble(byte[] bytes) {
        long l = toLong(bytes);
        return Double.longBitsToDouble(l);
    }

    /**
     * byte array to hex
     *
     * @param bytes byte array
     * @return String
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(toHexString(b));
        }
        return sb.toString();
    }

    /**
     * hex to byte array
     *
     * @param source the string value
     * @return byte array
     * @throws NullPointerException NullPointerException
     */
    public static byte[] parseHexStringToArray(String source) throws NullPointerException {
        int len = source.length();
        if (len % 2 != 0) {
            return null;
        }
        int size = len / 2;
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            String sub = source.substring(i * 2, i * 2 + 2);
            data[i] = parseHexString(sub);
        }
        return data;
    }

    /**
     * byte数组转无符号long
     *
     * @param bytes byte array
     * @return Long value
     * @throws Exception Exception
     */
    public static Long getUnsignedInt(byte[] bytes) throws Exception {
        int len = bytes.length;
        if (len < 1) {
            throw new Exception("can not cast null to int!!!");
        }
        return (long) (len - 4 >= 0 ? bytes[len - 4] & 0xFF : 0x00) << 24 | (long) (len - 3 >= 0 ? bytes[len - 3] & 0xFF : 0x00) << 16 | (long) (len - 2 >= 0 ? bytes[len - 2] & 0xFF : 0x00) << 8 | (long) (bytes[len - 1] & 0xFF);
    }

}

package com.chua.utils.tools.common.codec.binary;

import java.math.BigInteger;

import static com.chua.utils.tools.constant.NumberConstant.*;
import static com.chua.utils.tools.constant.SymbolConstant.ZERO;

/**
 * 进制转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/27
 */
public class BinaryFormat {

    /**
     * 将十进制的数字转换为指定进制的字符串
     *
     * @param value 十进制的数字
     * @param base  指定的进制
     * @return
     */
    public String fromDecimal(BigInteger value, int base) {
        return null == value ? null : value.toString(base);
    }

    /**
     * 将十进制的数字转换为十六进制的字符串
     *
     * @param value 十进制的数字
     * @return hex
     */
    public String fromDecimal(BigInteger value) {
        return null == value ? null : value.toString(16);
    }

    /**
     * 将十进制的数字转换为指定进制的字符串
     *
     * @param value 十进制的数字
     * @param base  指定的进制
     * @return
     */
    public String fromDecimal(long value, int base) {
        String string = Long.toString(value, base);
        if(base != HEX) {
            return string;
        }
        if(string.length() == 1) {
            return ZERO + string;
        }
        return string;
    }

    /**
     * 将十进制的数字转换为十六进制的字符串
     *
     * @param value 十进制的数字
     * @return hex
     */
    public String fromDecimal(long value) {
        String string = Long.toHexString(value);
        if(string.length() == 1) {
            return ZERO + string;
        }
        return string;
    }

    /**
     * 将十进制的数字转换为指定进制的字符串
     *
     * @param value 十进制的数字
     * @param base  指定的进制
     * @return
     */
    public String fromDecimal(int value, int base) {
        String string = Integer.toString(value, base).toUpperCase();
        if(base != HEX) {
            return string;
        }
        if(string.length() == 1) {
            return ZERO + string;
        }
        return string;
    }

    /**
     * 将十进制的数字转换为十六进制的字符串
     *
     * @param value 十进制的数字
     * @return hex
     */
    public String fromDecimal(int value) {
        String string = Integer.toHexString(value);
        if(string.length() == 1) {
            return ZERO + string;
        }
        return string;
    }

    /**
     * 将其它进制的数字（字符串形式）转换为十进制的数字
     *
     * @param str  其它进制的数字（字符串形式）
     * @param base 指定的进制
     * @return
     */
    public int toDecimal(String str, int base) {
        if (null == str) {
            return -1;
        }
        if (str.startsWith(HEX_LOWER)) {
            str = str.substring(HEX_LOWER.length());
        } else if (str.startsWith(HEX_UPPER)) {
            str = str.substring(HEX_LOWER.length());
        }

        return Integer.parseInt(str, base);
    }

    /**
     * 将其它进制的数字（字符串形式）转换为十进制的数字
     *
     * @param str  其它进制的数字（字符串形式）
     * @param base 指定的进制
     * @return
     */
    public long toDecimalLongValue(String str, int base) {
        if (null == str) {
            return -1;
        }
        if (str.startsWith(HEX_LOWER)) {
            str = str.substring(HEX_LOWER.length());
        } else if (str.startsWith(HEX_UPPER)) {
            str = str.substring(HEX_LOWER.length());
        }
        return Long.parseLong(str, base);
    }
}

package com.chua.utils.tools.common.codec.binary;

import com.chua.utils.tools.empty.EmptyOrBase;

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
     * @param value    十进制的数字
     * @param base 指定的进制
     * @return
     */
    public String fromDecimal(long value, int base) {
        long num = 0;
        if (value < 0) {
            num = ((long) 2 * 0x7fffffff) + value + 2;
        } else {
            num = value;
        }
        char[] buf = new char[32];
        int charPos = 32;
        while ((num / base) > 0) {
            buf[--charPos] = EmptyOrBase.DIGITS[(int) (num % base)];
            num /= base;
        }
        buf[--charPos] = EmptyOrBase.DIGITS[(int) (num % base)];
        return new String(buf, charPos, (32 - charPos));
    }

    /**
     * 将其它进制的数字（字符串形式）转换为十进制的数字
     *
     * @param str  其它进制的数字（字符串形式）
     * @param base 指定的进制
     * @return
     */
    public long toDecimal(String str, int base) {
        char[] buf = new char[str.length()];
        str.getChars(0, str.length(), buf, 0);
        long num = 0;
        for (int i = 0; i < buf.length; i++) {
            for (int j = 0; j < EmptyOrBase.DIGITS.length; j++) {
                if (EmptyOrBase.DIGITS[j] == buf[i]) {
                    num += j * Math.pow(base, buf.length - i - 1);
                    break;
                }
            }
        }
        return num;
    }
}

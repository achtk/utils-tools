package com.chua.utils.tools.operator.constant;

/**
 * 常量
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
public class CommonConstant {

    /**
     * Tabulator column increment.
     */
    public final static int TAB_INC = 8;

    /**
     * Tabulator character.
     */
    public final static byte TAB = 0x8;

    /**
     * Line feed character.
     */
    public final static byte LF = 0xA;

    /**
     * Form feed character.
     */
    public final static byte FF = 0xC;

    /**
     * Carriage return character.
     */
    public final static byte CR = 0xD;

    /**
     * QS_TODO 为什么不是0x0？<br/>
     * End of input character. Used as a sentinel to denote the character one beyond the last defined character in a
     * source file.
     */
    public final static byte EOI = 0x1A;
}

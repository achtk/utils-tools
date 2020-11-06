package com.chua.utils.tools.example;

import com.chua.utils.tools.common.codec.binary.Hex;

import java.nio.charset.Charset;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class HexExample {
    public static void main(String[] args) {
        String str = "172.16.55.35\tCLR_SAL\t3\t店员c\t\t0\t0\t0\t\t\r\n";
        String source = "3137322e31362e35352e333509434c525f53414c093309b5ead4b1630909300930093009090d0a";

        String gb2312Hex = Hex.convertStringToHex(str, Charset.forName("GB2312"));
        System.out.println(Hex.everySpace(gb2312Hex));
        System.out.println(Hex.everySpace(source));

        System.out.println(Hex.convertHexToString(gb2312Hex, Charset.forName("GB2312")));
    }
}

package com.chua.utils.tools.example;

import com.chua.utils.tools.common.codec.binary.Hex;

import java.nio.charset.Charset;

/**
 * 十六进制
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class HexExample {
    public static void main(String[] args) {
        String str = "172.16.55.35\tCLR_SAL\t3\t店员c\t\t0\t0\t0\t\t\r\n";
        String source =
                "01005FD0603402F1011806080F3836323136373035333033323337350301CA2B96" +
                "709CFE38363231363730353330333233373501043034363002303004363736";
        System.out.println(source.length());
        String gb2312Hex = Hex.convertStringToHex(str, Charset.forName("GB2312"));
        System.out.println(Hex.everySpace(gb2312Hex));
        System.out.println(Hex.everySpace(source));

        System.out.println(Hex.convertHexToString(gb2312Hex, Charset.forName("GB2312")));
    }
}

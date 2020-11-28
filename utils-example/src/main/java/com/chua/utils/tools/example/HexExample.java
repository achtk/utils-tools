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
        String source = "3137322e31362e35352e333509434c525f53414c093309b5ead4b1630909300930093009090d0a";

        String gb2312Hex = Hex.convertStringToHex(str, Charset.forName("GB2312"));
        System.out.println(Hex.everySpace(gb2312Hex));
        System.out.println(Hex.everySpace(source));
        System.out.println(Hex.everySpace("01005FBDCA8102F1019700240F3836363731343034373732353437370301CA2B96DF65B638363637313430343737323534373701043034363002303004363736460830423743423330311807043034363002303004363736460830434130463034370430343630023030043637364608303633313844313504303436300230300436373646083042374342333039043034363002303004363736460830363331384430320430343630023030043637364608303633313844303104303436300230300436373646083043353233393432043034363002303004363736460830363331384431360ACC08FB069B31A50C54502D4C494E4B5F3942333154759516A5CFD00C54502D4C494E4B5F41354346ECF8EB61A611AC0D4368696E614E65742D616D48568CA6DF586B6DBD0C54502D4C494E4B5F36423644487D2E32EE37C7055A4E53484A480EECAFC419C8076E65697869616F94D9B38446BDA90C54502D4C494E4B5F34364244FC0A81CECF80A7097878626172636F646508107BCF12F3BB0E4E6574636F72652D434631324633EC89141C1E16CF0A594A5A2D4C41594F5554"));
        System.out.println(Hex.everySpace("01005F2CBD7202F100131C2C0F3836363731343034343931333031390F"));
        System.out.println(Hex.every0xSpace("01005FBDCA8102F1019700240F3836363731343034373732353437370301CA2B96DF65B638363637313430343737323534373701043034363002303004363736460830423743423330311807043034363002303004363736460830434130463034370430343630023030043637364608303633313844313504303436300230300436373646083042374342333039043034363002303004363736460830363331384430320430343630023030043637364608303633313844303104303436300230300436373646083043353233393432043034363002303004363736460830363331384431360ACC08FB069B31A50C54502D4C494E4B5F3942333154759516A5CFD00C54502D4C494E4B5F41354346ECF8EB61A611AC0D4368696E614E65742D616D48568CA6DF586B6DBD0C54502D4C494E4B5F36423644487D2E32EE37C7055A4E53484A480EECAFC419C8076E65697869616F94D9B38446BDA90C54502D4C494E4B5F34364244FC0A81CECF80A7097878626172636F646508107BCF12F3BB0E4E6574636F72652D434631324633EC89141C1E16CF0A594A5A2D4C41594F5554"));
        System.out.println(Hex.every0xSpace("01005F2CBD7202F100131C2C0F3836363731343034343931333031390F"));

        System.out.println(Hex.convertHexToString(gb2312Hex, Charset.forName("GB2312")));
    }
}

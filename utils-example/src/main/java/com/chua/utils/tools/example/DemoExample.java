package com.chua.utils.tools.example;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/11
 */
public class DemoExample {

    public static void main(String[] args) {
        String hash = "";
        String txt = "39";

        for (int i = 0; i < 100000000; i++) {
            txt = DigestUtils.md2Hex(txt);
            System.out.println(i + ":" + txt);
        }

        System.out.println(hash);

    }

}

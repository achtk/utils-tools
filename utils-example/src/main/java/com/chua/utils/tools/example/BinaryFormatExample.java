package com.chua.utils.tools.example;

import com.chua.utils.tools.common.codec.binary.BinaryFormat;

/**
 * 进制转化
 * @author CH
 * @version 1.0.0
 * @since 2020/11/27
 */
public class BinaryFormatExample {

    public static void main(String[] args) {
        BinaryFormat binaryFormat = new BinaryFormat();
        System.out.println("十进制10转为十六进制: " + binaryFormat.fromDecimal(10, 16));
        System.out.println("十进制10转为十进制: " + binaryFormat.fromDecimal(10, 10));
        System.out.println("十进制10转为八进制: " + binaryFormat.fromDecimal(10, 8));
        System.out.println("十进制10转为二进制: " + binaryFormat.fromDecimal(10, 2));

        System.out.println("十六进制C转为十进制: " + binaryFormat.toDecimal("10", 10));
        System.out.println("十进制10转为十进制: " + binaryFormat.toDecimal("10", 10));
    }
}

package com.chua.tools.example;

import com.chua.utils.tools.common.codec.binary.BinaryFormat;

/**
 * 进制转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/27
 */
public class BinaryFormatExample extends BaseExample {

    public static void main(String[] args) {
        BinaryFormat binaryFormat = new BinaryFormat();
        log.info("十进制10转为十六进制: " + binaryFormat.fromDecimal(10, 16));
        log.info("十进制160转为十六进制: " + binaryFormat.fromDecimal(160, 16));
        log.info("十进制10转为十进制: " + binaryFormat.fromDecimal(10, 10));
        log.info("十进制10转为八进制: " + binaryFormat.fromDecimal(10, 8));
        log.info("十进制10转为二进制: " + binaryFormat.fromDecimal(10, 2));
        log.info("十六进制0x00a0转为十进制: " + binaryFormat.toDecimal("0x00a0", 16));
        log.info("十进制10转为十进制: " + binaryFormat.toDecimal("0x1010", 16));
    }
}

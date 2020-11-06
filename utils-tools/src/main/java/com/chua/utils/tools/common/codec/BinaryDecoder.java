package com.chua.utils.tools.common.codec;

/**
 * 二进制解密方式
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/4
 */
public interface BinaryDecoder extends Decoder {
    /**
     * 解密方式
     *
     * @param source 数据源
     * @return 加密数据
     * @throws Exception Exception
     */
    byte[] decode(byte[] source) throws Exception;
}

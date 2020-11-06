package com.chua.utils.tools.common.codec;

/**
 * 二进制加密方式
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/4
 */
public interface BinaryEncoder extends Encoder {
    /**
     * 加密方式
     *
     * @param source 数据源
     * @return 加密数据
     * @throws Exception Exception
     */
    byte[] encode(byte[] source) throws Exception;
}

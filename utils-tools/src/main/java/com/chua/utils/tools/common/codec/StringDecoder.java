package com.chua.utils.tools.common.codec;

/**
 * 字符串解密方式
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/4
 */
public interface StringDecoder extends Encoder {
    /**
     * 解密方式
     *
     * @param source 数据
     * @return 解密数据
     * @throws Exception Exception
     */
    String decode(String source) throws Exception;
}

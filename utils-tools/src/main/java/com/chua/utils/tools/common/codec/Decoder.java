package com.chua.utils.tools.common.codec;

/**
 * 解密方式
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/4
 */
public interface Decoder {

    /**
     * 解密方式
     *
     * @param source 数据源
     * @return 加密数据
     * @throws Exception Exception
     */
    Object decode(Object source) throws Exception;
}

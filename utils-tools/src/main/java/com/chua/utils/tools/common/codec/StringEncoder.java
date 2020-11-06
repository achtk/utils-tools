package com.chua.utils.tools.common.codec;

/**
 * 字符串加密方式
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/4
 */
public interface StringEncoder extends Encoder {
    /**
     * 加密方式
     *
     * @param source 数据
     * @return 加密数据
     * @throws Exception Exception
     */
    String encode(String source) throws Exception;
}

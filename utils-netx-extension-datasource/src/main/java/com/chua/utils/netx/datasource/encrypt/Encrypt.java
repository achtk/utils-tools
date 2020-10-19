package com.chua.utils.netx.datasource.encrypt;

/**
 * 加解密
 * @author CH
 * @version 1.0
 * @date 2020/10/19 20:08
 */
public interface Encrypt {
    /**
     * 密码加密
     * @param passwd
     * @return
     */
    String encode(String passwd);

    /**
     * 密码解密
     * @param passwd
     * @return
     */
    String decode(String passwd);
}

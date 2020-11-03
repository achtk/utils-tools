package com.chua.utils.tools.encrypt;

import java.util.Map;

/**
 * 获取密钥
 * @author CH
 */
public interface EncryptKeySpec {

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    byte[] getPrivateKey(Map<String, Object> keyMap);

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    byte[] getPublicKey(Map<String, Object> keyMap) throws Exception;
}

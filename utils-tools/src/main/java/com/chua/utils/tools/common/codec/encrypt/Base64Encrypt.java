package com.chua.utils.tools.common.codec.encrypt;

import java.util.Base64;

/**
 * base64
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/6
 */
public class Base64Encrypt extends AbstractStandardEncrypt {

    final Base64.Decoder decoder = Base64.getDecoder();
    final Base64.Encoder encoder = Base64.getEncoder();

    @Override
    public byte[] decode(byte[] source) throws Exception {
        return decoder.decode(source);
    }

    @Override
    public byte[] encode(byte[] source) throws Exception {
        return encoder.encode(source);
    }
}

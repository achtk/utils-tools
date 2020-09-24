package com.chua.utils.tools.encrypt;

import com.chua.utils.tools.encrypt.sun.misc.BASE64Decoder;
import com.chua.utils.tools.encrypt.sun.misc.BASE64Encoder;
import com.google.common.base.Charsets;

import java.io.IOException;

/**
 * base64
 * @author CH
 */
public class Base64Encrypt implements IEncrypt {

    private static final BASE64Encoder BASE_64_ENCODER = new BASE64Encoder();
    private static final BASE64Decoder BASE_64_DECODER = new BASE64Decoder();

    @Override
    public byte[] encode(byte[] bytes) {
        String buffer = BASE_64_ENCODER.encodeBuffer(bytes);
        return buffer.getBytes(Charsets.UTF_8);
    }

    @Override
    public byte[] decode(byte[] bytes) {
        String buffer = new String(bytes, Charsets.UTF_8);
        try {
            return BASE_64_DECODER.decodeBuffer(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

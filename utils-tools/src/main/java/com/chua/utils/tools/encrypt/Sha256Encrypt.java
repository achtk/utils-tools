package com.chua.utils.tools.encrypt;

import com.chua.utils.tools.constant.EncryptConstant;

import java.security.MessageDigest;

/**
 * Sha256
 * @author CH
 */
public class Sha256Encrypt implements Encrypt {

    @Override
    public byte[] encode(byte[] bytes) {
        MessageDigest messageDigest = messageDigest(EncryptConstant.SHA_256);
        messageDigest.update(bytes);
        return messageDigest.digest();
    }

    @Override
    public byte[] decode(byte[] bytes) {
        return null;
    }


}

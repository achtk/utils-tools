package com.chua.utils.tools.encrypt;

import com.chua.utils.tools.constant.EncryptConstant;

import java.security.MessageDigest;

/**
 * Sha512
 * @author CH
 */
public class Sha512Encrypt implements IEncrypt {

    @Override
    public byte[] encode(byte[] bytes) {
        MessageDigest messageDigest = messageDigest(EncryptConstant.SHA_512);
        messageDigest.update(bytes);
        return messageDigest.digest();
    }

    @Override
    public byte[] decode(byte[] bytes) {
        return null;
    }
}

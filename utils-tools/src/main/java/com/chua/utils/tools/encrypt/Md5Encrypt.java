package com.chua.utils.tools.encrypt;

import com.chua.utils.tools.constant.EncryptConstant;

import java.security.MessageDigest;

/**
 * MD5
 * @author CH
 */
public class Md5Encrypt implements IEncrypt {

    @Override
    public byte[] encode(byte[] bytes) {
        MessageDigest messageDigest = messageDigest(EncryptConstant.MD5);
        messageDigest.update(bytes);
        return messageDigest.digest();
    }

    @Override
    public byte[] decode(byte[] bytes) {
        return null;
    }
}

package com.chua.utils.tools.common.codec.encrypt;

import com.chua.utils.tools.common.codec.digest.DigestHelper;
import com.chua.utils.tools.constant.EncryptConstant;

import java.security.MessageDigest;

/**
 * Sha256
 *
 * @author CH
 */
public class Sha256Encrypt extends AbstractStandardEncrypt {

    @Override
    public byte[] encode(byte[] bytes) {
        return DigestHelper.sha256(bytes);
    }

    @Override
    public byte[] decode(byte[] bytes) {
        return null;
    }


}

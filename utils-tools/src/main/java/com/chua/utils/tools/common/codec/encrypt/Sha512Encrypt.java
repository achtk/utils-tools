package com.chua.utils.tools.common.codec.encrypt;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;

/**
 * Sha512 加密
 *
 * @author CH
 */
public class Sha512Encrypt extends AbstractStandardEncrypt {

    @Override
    public byte[] encode(byte[] bytes) {
        Digest digest = new SHA512Digest();
        digest.update(bytes, 0, bytes.length);
        byte[] result = new byte[digest.getDigestSize()];
        digest.doFinal(result, 0);
        return result;
    }

    @Override
    public byte[] decode(byte[] bytes) {
        return null;
    }
}

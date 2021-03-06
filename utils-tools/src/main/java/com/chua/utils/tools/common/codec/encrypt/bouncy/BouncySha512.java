package com.chua.utils.tools.common.codec.encrypt.bouncy;

import com.chua.utils.tools.common.codec.encrypt.AbstractStandardEncrypt;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;

/**
 * 基于bouncy实现的sha512
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
public class BouncySha512 extends AbstractStandardEncrypt {
    @Override
    public byte[] decode(byte[] source) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] encode(byte[] bytes) throws Exception {
        Digest digest = new SHA512Digest();
        digest.update(bytes, 0, bytes.length);
        byte[] result = new byte[digest.getDigestSize()];
        digest.doFinal(result, 0);
        return result;
    }
}

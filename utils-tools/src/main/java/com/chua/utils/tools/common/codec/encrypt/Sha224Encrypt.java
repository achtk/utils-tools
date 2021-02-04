package com.chua.utils.tools.common.codec.encrypt;

import com.chua.utils.tools.common.codec.encrypt.bouncy.BouncySha224;
import com.chua.utils.tools.util.ClassUtils;

import java.security.MessageDigest;

/**
 * Sha224
 *
 * @author CH
 */
public class Sha224Encrypt extends AbstractStandardEncrypt {

    private static final String SHA = "org.bouncycastle.crypto.digests.SHA224Digest";

    @Override
    public byte[] encode(byte[] bytes) {
        try {
            if (!ClassUtils.isPresent(SHA)) {
                MessageDigest sha1 = MessageDigest.getInstance("SHA-224");
                sha1.update(bytes);
                return sha1.digest();
            }
            Encrypt encrypt = new BouncySha224();
            return encrypt.encode(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decode(byte[] bytes) {
        return null;
    }


}

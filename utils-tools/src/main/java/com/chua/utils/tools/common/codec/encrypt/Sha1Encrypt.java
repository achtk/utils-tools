package com.chua.utils.tools.common.codec.encrypt;

import com.chua.utils.tools.common.codec.encrypt.bouncy.BouncySha1;
import com.chua.utils.tools.util.ClassUtils;

import java.security.MessageDigest;

/**
 * Sha1
 *
 * @author CH
 */
public class Sha1Encrypt extends AbstractStandardEncrypt {

    private static final String SHA1 = "org.bouncycastle.crypto.digests.SHA1Digest";

    @Override
    public byte[] encode(byte[] bytes) {
        try {
            if (!ClassUtils.isPresent(SHA1)) {
                MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
                sha1.update(bytes);
                return sha1.digest();
            }
            Encrypt encrypt = new BouncySha1();
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

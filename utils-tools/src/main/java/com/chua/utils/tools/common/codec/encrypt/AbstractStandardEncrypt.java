package com.chua.utils.tools.common.codec.encrypt;

import com.chua.utils.tools.common.charset.CharsetHelper;
import com.chua.utils.tools.common.codec.binary.Hex;
import com.chua.utils.tools.environment.StandardEnvironment;
import com.chua.utils.tools.exceptions.NotSupportedException;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * 标准的加解密
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/4
 */
public abstract class AbstractStandardEncrypt extends StandardEnvironment implements Encrypt {

    /**
     *
     */
    public static final byte DEFAULT = 0x7e;
    /**
     * key
     */
    public static final String ENCRYPT_KEY = "encrypt";
    /**
     * 默认key
     */
    public static final String DEFAULT_ENCRYPT_KEY_VALUE = "1234567890";

    @Override
    public String decode(String source) throws Exception {
        byte[] decode = decode(Hex.decodeHex(source));
        if(null == decode) {
            return null;
        }
        return new String(decode);
    }

    @Override
    public String encode(String source) throws Exception {
        return Hex.encodeHexString(encode(CharsetHelper.getBytesUtf8(source)));
    }

    @Override
    public Object decode(Object source) throws Exception {
        throw new NotSupportedException();
    }

    @Override
    public Object encode(Object source) throws Exception {
        throw new NotSupportedException();
    }

    /**
     * 获取值
     *
     * @param name 索引
     * @return 值
     */
    protected String getValue(String name) {
        Object object = getObject(name);
        return null == object ? null : object.toString();
    }

    /**
     * 获取索引
     *
     * @return
     */
    protected byte[] getKey(final int size) {
        if (!container(ENCRYPT_KEY)) {
            return new byte[0];
        }
        byte[] bytes = getStringOrDefault(ENCRYPT_KEY, DEFAULT_ENCRYPT_KEY_VALUE).getBytes(UTF_8);
        if (size == -1) {
            return bytes;
        }
        if (bytes.length < size) {
            byte[] bytes1 = new byte[size];
            System.arraycopy(bytes, 0, bytes1, 0, bytes.length);
            return bytes1;
        }
        return bytes;
    }
}

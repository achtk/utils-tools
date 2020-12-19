package com.chua.utils.tools.common.codec;

import com.chua.utils.tools.common.codec.binary.Hex;
import com.chua.utils.tools.util.CharUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

/**
 * URL解码器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public class UrlEncoder implements StringEncoder {

    /**
     * 默认{@link UrlEncoder}<br>
     * 默认的编码器针对URI路径编码，定义如下：
     *
     * <pre>
     * pchar = unreserved（不处理） / pct-encoded / sub-delims（子分隔符） / ":" / "@"
     * unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~"
     * sub-delims = "!" / "$" / "&amp;" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
     * </pre>
     */
    public static final UrlEncoder DEFAULT = createDefault();
    /**
     * 存放安全编码
     */
    private final BitSet safeCharacters;

    /**
     * 是否编码空格为+
     */
    private boolean encodeSpaceAsPlus = false;

    /**
     * 构造<br>
     * <p>
     * [a-zA-Z0-9]默认不被编码
     */
    public UrlEncoder() {
        this(new BitSet(256));

        for (char i = 'a'; i <= 'z'; i++) {
            addSafeCharacter(i);
        }
        for (char i = 'A'; i <= 'Z'; i++) {
            addSafeCharacter(i);
        }
        for (char i = '0'; i <= '9'; i++) {
            addSafeCharacter(i);
        }
    }

    /**
     * 构造
     *
     * @param safeCharacters 安全字符，安全字符不被编码
     */
    private UrlEncoder(BitSet safeCharacters) {
        this.safeCharacters = safeCharacters;
    }

    /**
     * 创建默认{@link UrlEncoder}<br>
     * 默认的编码器针对URI路径编码，定义如下：
     *
     * <pre>
     * pchar = unreserved（不处理） / pct-encoded / sub-delims（子分隔符） / ":" / "@"
     * unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~"
     * sub-delims = "!" / "$" / "&amp;" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
     * </pre>
     *
     * @return {@link UrlEncoder}
     */
    public static UrlEncoder createDefault() {
        final UrlEncoder encoder = new UrlEncoder();
        encoder.addSafeCharacter('-');
        encoder.addSafeCharacter('.');
        encoder.addSafeCharacter('_');
        encoder.addSafeCharacter('~');
        // Add the sub-delims
        encoder.addSafeCharacter('!');
        encoder.addSafeCharacter('$');
        encoder.addSafeCharacter('&');
        encoder.addSafeCharacter('\'');
        encoder.addSafeCharacter('(');
        encoder.addSafeCharacter(')');
        encoder.addSafeCharacter('*');
        encoder.addSafeCharacter('+');
        encoder.addSafeCharacter(',');
        encoder.addSafeCharacter(';');
        encoder.addSafeCharacter('=');
        // Add the remaining literals
        encoder.addSafeCharacter(':');
        encoder.addSafeCharacter('@');
        // Add '/' so it isn't encoded when we encode a path
        encoder.addSafeCharacter('/');

        return encoder;
    }

    /**
     * 增加安全字符<br>
     * 安全字符不被编码
     *
     * @param c 字符
     */
    public void addSafeCharacter(char c) {
        safeCharacters.set(c);
    }

    @Override
    public String encode(String source) {
        final StringBuilder rewrittenPath = new StringBuilder(source.length());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(buf, StandardCharsets.UTF_8);

        int c;
        for (int i = 0; i < source.length(); i++) {
            c = source.charAt(i);
            if (safeCharacters.get(c)) {
                rewrittenPath.append((char) c);
            } else if (encodeSpaceAsPlus && c == CharUtils.SPACE) {
                // 对于空格单独处理
                rewrittenPath.append('+');
            } else {
                // convert to external encoding before hex conversion
                try {
                    writer.write((char) c);
                    writer.flush();
                } catch (IOException e) {
                    buf.reset();
                    continue;
                }

                byte[] ba = buf.toByteArray();
                for (byte toEncode : ba) {
                    // Converting each byte in the buffer
                    rewrittenPath.append('%');
                    Hex.appendHex(rewrittenPath, toEncode, false);
                }
                buf.reset();
            }
        }
        return rewrittenPath.toString();
    }

    @Override
    public Object encode(Object source) throws Exception {
        return encode(source.toString());
    }
}

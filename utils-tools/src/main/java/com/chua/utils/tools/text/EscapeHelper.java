package com.chua.utils.tools.text;

import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.function.Filter;

/**
 * 转义和反转义工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class EscapeHelper {

    /**
     * 不转义的符号编码
     */
    private static final String NOT_ESCAPE_CHARS = "*@-_+./";
    private static final Filter<Character> JS_ESCAPE_FILTER = c -> !(
            Character.isDigit(c)
                    || Character.isLowerCase(c)
                    || Character.isUpperCase(c)
                    || NOT_ESCAPE_CHARS.indexOf(c) != -1
    );

    /**
     * Escape编码（Unicode）<br>
     *
     * @param content 元数据
     * @return 编码后的字符串
     */
    public static String escape(CharSequence content) {
        if (StringHelper.isEmpty(content)) {
            return content.toString();
        }

        final StringBuilder tmp = new StringBuilder(content.length() * 6);
        for (int i = 0; i < content.length(); i++) {
            char chars = content.charAt(i);
            if (!JS_ESCAPE_FILTER.matcher(chars)) {
                tmp.append(chars);
            } else if (chars < 256) {
                tmp.append("%");
                if (chars < 16) {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(chars, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(chars, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * Escape解码
     *
     * @param content 被转义的内容
     * @return 解码后的字符串
     */
    public static String unescape(String content) {
        if (StringHelper.isEmpty(content)) {
            return content;
        }

        StringBuilder tmp = new StringBuilder(content.length());
        int lastPos = 0, length = content.length();
        while (lastPos < length) {
            int pos = content.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (content.charAt(pos + 1) == 'u') {
                    tmp.append(Integer.parseInt(content.substring(pos + 2, pos + 6), 16));
                    lastPos = pos + 6;
                    continue;
                }
                tmp.append(Integer.parseInt(content.substring(pos + 1, pos + 3), 16));
                lastPos = pos + 3;
                continue;
            }
            if (pos == -1) {
                tmp.append(content.substring(lastPos));
                lastPos = content.length();
                continue;
            }
            tmp.append(content, lastPos, pos);
            lastPos = pos;
        }
        return tmp.toString();
    }
}

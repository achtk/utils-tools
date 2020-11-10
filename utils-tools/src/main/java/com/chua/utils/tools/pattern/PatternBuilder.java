package com.chua.utils.tools.pattern;

import com.chua.utils.tools.constant.PatternConstant;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 正则构造器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public class PatternBuilder implements Appendable {

    private final StringBuilder stringBuilder = new StringBuilder();

    @Override
    public PatternBuilder append(CharSequence csq) {
        stringBuilder.append(csq);
        return this;
    }

    @Override
    public PatternBuilder append(CharSequence csq, int start, int end) {
        stringBuilder.append(csq, start, end);
        return this;
    }

    @Override
    public PatternBuilder append(char c) {
        stringBuilder.append(c);
        return this;
    }

    /**
     * 添加\\s+
     *
     * @return this
     * @throws IOException
     */
    public PatternBuilder appendEmpty() {
        return append(PatternConstant.PATTERN_EMPTY.pattern());
    }

    /**
     * 添加\\w
     * <p>Any word character</p>
     *
     * @return this
     * @throws IOException
     */
    public PatternBuilder appendAnyChar() {
        return append("(\\w)");
    }

    /**
     * 添加 (.*?)
     *
     * @return this
     * @throws IOException
     */
    public PatternBuilder appendAny() {
        return append(PatternConstant.ALL_PATTERN.pattern());
    }

    /**
     * 获取正则
     *
     * @return 正则
     */
    public Pattern toPattern() {
        return Pattern.compile(stringBuilder.toString());
    }
}

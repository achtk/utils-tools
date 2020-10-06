package com.chua.utils.tools.function;

import java.util.regex.Matcher;

/**
 * 回调
 * @author CH
 */
public interface IPreMatcher<I> {
    /**
     * 处理下一个
     * @param item
     * @return
     */
    I matcher(Matcher item);
}

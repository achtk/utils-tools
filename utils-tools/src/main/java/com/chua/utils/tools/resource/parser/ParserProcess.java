package com.chua.utils.tools.resource.parser;

import com.chua.utils.tools.function.Matcher;

/**
 * 解析过程
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public interface ParserProcess<T> {
    /**
     * 解析过程
     *
     * @param matcher 匹配
     */
    void doWith(Matcher<T> matcher);
}

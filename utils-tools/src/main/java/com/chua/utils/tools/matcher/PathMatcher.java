package com.chua.utils.tools.matcher;

/**
 * 路径匹配
 *
 * @author CH
 * @since 1.0
 */
public interface PathMatcher {
    /**
     * 是否满足匹配标准
     *
     * @param path 路径
     * @return boolean
     */
    boolean isPattern(String path);

    /**
     * 匹配
     *
     * @param pattern 正则
     * @param path    路径
     * @return boolean
     */
    boolean match(String pattern, String path);

    /**
     * 匹配
     *
     * @param pattern 正则
     * @param path    路径
     * @return boolean
     */
    boolean matchStart(String pattern, String path);
}

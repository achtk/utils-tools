package com.chua.utils.tools.matcher;

import com.chua.utils.tools.common.StringHelper;

/**
 * apache-io 数据匹配
 *
 * @author CH
 * @since 1.0
 */
public class ApachePathMatcher implements PathMatcher {

    @Override
    public boolean isPattern(String path) {
        return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
    }

    @Override
    public boolean match(String pattern, String path) {
        return StringHelper.wildcardMatch(path, pattern);
    }

    @Override
    public boolean matchStart(String pattern, String path) {
        return StringHelper.wildcardMatch(path, pattern);
    }

}

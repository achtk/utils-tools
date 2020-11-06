package com.chua.utils.tools.resource.matcher;


import com.chua.utils.tools.resource.Resource;

import java.util.Set;

/**
 * 路径匹配
 * @author CH
 * @since 1.0
 */
public interface IPathMatcher {
    /**
     * 匹配资源
     * @return 资源
     * @throws Throwable Throwable
     */
    public Set<Resource> matcher() throws Throwable;
}

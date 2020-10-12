package com.chua.utils.tools.filter;

import java.lang.reflect.Method;

/**
 * 方法过滤
 * @author CH
 * @version 1.0.0
 * @since 2020/10/12
 */
@FunctionalInterface
public interface MethodFilter {

    /**
     * @param method 方法
     * @return boolean
     */
    boolean matches(Method method);
}

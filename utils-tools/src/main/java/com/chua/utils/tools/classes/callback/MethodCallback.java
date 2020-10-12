package com.chua.utils.tools.classes.callback;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/12
 */

import java.lang.reflect.Method;

/**
 * 字方法回调
 */
@FunctionalInterface
public interface MethodCallback {

    /**
     * 方法回调
     * @param method
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
}

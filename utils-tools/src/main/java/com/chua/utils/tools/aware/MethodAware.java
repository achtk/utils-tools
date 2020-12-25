package com.chua.utils.tools.aware;

import java.lang.reflect.Method;

/**
 * 方法提供接口
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
public interface MethodAware {
    /**
     * 设置方法
     * @param method 方法
     */
    void setField(Method method);
}

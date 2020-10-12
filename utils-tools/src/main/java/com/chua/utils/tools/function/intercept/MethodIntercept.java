package com.chua.utils.tools.function.intercept;

import java.lang.reflect.Method;

/**
 * 方法拦截器
 * @author CH
 */
public interface MethodIntercept {

    /**
     * 执行方法
     * @param obj 对象
     * @param method 方法
     * @param args 参数
     * @param proxy proxy
     * @exception Exception
     * @return
     */
    public Object invoke(Object obj, Method method, Object[] args, Object... proxy) throws Throwable;
}

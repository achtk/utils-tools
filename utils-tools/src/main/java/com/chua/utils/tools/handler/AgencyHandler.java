package com.chua.utils.tools.handler;

import java.lang.reflect.Method;

/**
 * 代理处理
 * @author CH
 */
public interface AgencyHandler {
    /**
     *  代理
     * @param obj 对象
     * @param method 方法
     * @param args 参数
     * @param proxy 方法代理
     * @return
     * @throws Throwable
     */
    public Object intercept(Object obj, Method method, Object[] args, Object... proxy) throws Throwable;
}

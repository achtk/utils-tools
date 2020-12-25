package com.chua.utils.tools.function;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 方法匹配
 * @author CH
 */
@FunctionalInterface
public interface MethodMatcher {
    /**
     * 方法拦截
     * @param obj 对象
     * @param method 方法
     * @param args 参数
     * @param proxy 代理方法
     * @return
     * @throws Throwable
     */
    Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable;
}

package com.chua.utils.tools.proxy;

import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.mapper.ProxyMapper;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 代理对象
 * @author CH
 */
public interface ProxyAgent<T> {

    /**
     * 代理
     * @param tClass 代理接口/类
     * @return
     */
    T newProxy(Class<T> tClass);

    /**
     * 执行方法
     * @param proxyMapper 代理映射
     * @param obj 对象
     * @param method 方法
     * @param args 参数
     * @param proxy 代理
     * @return
     * @throws Throwable
     */
    Object invoker(ProxyMapper proxyMapper, Object obj, Method method, Object[] args, Object... proxy) throws Throwable;
}

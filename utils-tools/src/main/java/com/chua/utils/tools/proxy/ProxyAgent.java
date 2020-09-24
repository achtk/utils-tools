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
public abstract class ProxyAgent<T> {

    private static final Set<String> SKIP = new HashSet<>();
    static {
        SKIP.add("toString");
    }
    /**
     * 映射代理
     */
    @Getter
    private ProxyMapper proxyMapper;
    /**
     * 类
     */
    @Getter
    private Class<T> source;

    /**
     * 方法以及拦截器
     */
    private Map<String, MethodIntercept> methodInterceptMap = new HashMap<>();

    public ProxyAgent(Class<T> source) {
        this.source = source;
    }

    public ProxyAgent(Class<T> source, ProxyMapper proxyMapper) {
        this.source = source;
        this.proxyMapper = proxyMapper;
    }

    public ProxyAgent(Class<T> source, MethodIntercept methodIntercept) {
        this.source = source;
        ProxyMapper proxyMapper = new ProxyMapper();
        proxyMapper.addMethodIntercept("*", methodIntercept);
        this.proxyMapper = proxyMapper;
    }

    /**
     * 代理
     * @return
     */
    abstract public T newProxy();

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
    abstract public Object invoker(ProxyMapper proxyMapper, Object obj, Method method, Object[] args, Object... proxy) throws Throwable;
}

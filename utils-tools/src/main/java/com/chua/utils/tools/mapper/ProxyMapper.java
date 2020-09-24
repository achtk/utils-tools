package com.chua.utils.tools.mapper;

import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.function.MethodIntercept;
import com.google.common.base.Strings;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理映射
 * @author CH
 */
@Getter
@EqualsAndHashCode
public class ProxyMapper {
    /**
     * 方法代理缓存
     */
    private static final ConcurrentHashMap<String, MethodIntercept> METHOD_INTERCEPT_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    /**
     * 方法代理缓存
     */
    private static final ConcurrentHashMap<String, String> METHOD_FUNCTIONS = new ConcurrentHashMap<>();

    /**
     * 执行方法
     * @param obj 对象
     * @param method 方法
     * @param args 参数
     * @param proxy 代理
     * @return
     * @throws Throwable
     */
    public static Object intercept(Object obj, Method method, Object[] args, Object proxy) throws Throwable {
        method.setAccessible(true);
        return method.invoke(obj, args);
    }
    /**
     * 添加代理
     * @param name 方法名
     * @param methodIntercept 方法拦截器
     * @return
     */
    public ProxyMapper addMethodIntercept(final String name, final MethodIntercept methodIntercept) {
        if(Strings.isNullOrEmpty(name)) {
            return this;
        }
        METHOD_INTERCEPT_CONCURRENT_HASH_MAP.put(name, methodIntercept);
        return this;
    }
    /**
     * 添加代理
     * @param name 方法名
     * @param methodIntercept 方法拦截器
     * @return
     */
    public ProxyMapper addFunctionIntercept(final String name, final String methodIntercept) {
        if(Strings.isNullOrEmpty(name)) {
            return this;
        }
        METHOD_FUNCTIONS.put(name, methodIntercept);
        return this;
    }

    /**
     * 是否包含方法
     * @param name 方法名称
     * @return boolean
     */
    public Boolean hasName(String name) {
        MethodIntercept methodIntercept = tryToGetProxy(name);
        if(null != methodIntercept) {
            return true;
        }
        return tryToGetProxyString(name) != null;
    }

    /**
     * 尝试获取方法
     * @param name 方法名称
     * @return
     */
    public MethodIntercept tryToGetProxy(String name) {
        for (Map.Entry<String, MethodIntercept> entry : METHOD_INTERCEPT_CONCURRENT_HASH_MAP.entrySet()) {
            if(!StringHelper.wildcardMatch(name, entry.getKey())) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }
    /**
     * 尝试获取方法
     * @param name 方法名称
     * @return
     */
    public String tryToGetProxyString(String name) {
        for (Map.Entry<String, String> entry : METHOD_FUNCTIONS.entrySet()) {
            if(!StringHelper.wildcardMatch(name, entry.getKey())) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }
}

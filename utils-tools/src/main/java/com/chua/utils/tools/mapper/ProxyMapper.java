package com.chua.utils.tools.mapper;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.collects.MultiValueSetMap;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.chua.utils.tools.loader.BalancerLoader;
import com.google.common.base.Strings;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 代理映射
 *
 * @author CH
 */
public class ProxyMapper {

    /**
     * 方法代理缓存
     */
    private static final MultiValueSetMap<String, MethodIntercept> CACHE_INTERCEPT = new MultiValueSetMap<>();

    /**
     * 执行方法
     *
     * @param obj    对象
     * @param method 方法
     * @param args   参数
     * @param proxy  代理
     * @return 方法
     * @throws Throwable Throwable
     */
    public static Object intercept(Object obj, Method method, Object[] args, Object proxy) throws Throwable {
        ClassHelper.methodAccessible(method);
        return method.invoke(obj, args);
    }

    /**
     * 添加代理
     *
     * @param name            方法名
     * @param methodIntercept 方法拦截器
     * @return ProxyMapper
     */
    public ProxyMapper addIntercept(final String name, final MethodIntercept methodIntercept) {
        if (Strings.isNullOrEmpty(name)) {
            return this;
        }
        CACHE_INTERCEPT.add(name, methodIntercept);
        return this;
    }

    /**
     * 是否包含方法
     *
     * @param name 方法名称
     * @return boolean
     */
    public Boolean hasName(String name) {
        return tryToGetProxy(name, null) != null;
    }

    /**
     * 尝试获取方法
     *
     * @param name           方法名称
     * @param balancerLoader 负载均衡加载器
     * @return MethodIntercept
     */
    public MethodIntercept tryToGetProxy(String name, BalancerLoader balancerLoader) {
        Set<String> keySet = CACHE_INTERCEPT.keySet();
        for (String s : keySet) {
            if (!StringHelper.wildcardMatch(name, s)) {
                continue;
            }
            Set<MethodIntercept> methodIntercepts = CACHE_INTERCEPT.get(s);
            if (null == balancerLoader) {
                return FinderHelper.firstElement(methodIntercepts);
            }
            balancerLoader.data(methodIntercepts);
            return (MethodIntercept) balancerLoader.balancer();
        }
        return null;
    }
}

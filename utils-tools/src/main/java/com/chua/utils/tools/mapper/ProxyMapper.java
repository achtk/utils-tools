package com.chua.utils.tools.mapper;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.loader.BalancerLoader;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import lombok.*;

import java.lang.reflect.Method;
import java.util.Set;

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
    private static final HashMultimap<String, MethodIntercept> CACHE_INTERCEPT = HashMultimap.create();

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
        ClassHelper.methodAccessible(method);
        return method.invoke(obj, args);
    }
    /**
     * 添加代理
     * @param name 方法名
     * @param methodIntercept 方法拦截器
     * @return
     */
    public ProxyMapper addIntercept(final String name, final MethodIntercept methodIntercept) {
        if(Strings.isNullOrEmpty(name)) {
            return this;
        }
        CACHE_INTERCEPT.put(name, methodIntercept);
        return this;
    }

    /**
     * 是否包含方法
     * @param name 方法名称
     * @return boolean
     */
    public Boolean hasName(String name) {
        return tryToGetProxy(name, null) != null;
    }

    /**
     * 尝试获取方法
     * @param name 方法名称
     * @param balancerLoader
     * @return
     */
    public MethodIntercept tryToGetProxy(String name, BalancerLoader balancerLoader) {
        Set<String> keySet = CACHE_INTERCEPT.keySet();
        for (String s : keySet) {
            if(!StringHelper.wildcardMatch(name, s)) {
                continue;
            }
            Set<MethodIntercept> methodIntercepts = CACHE_INTERCEPT.get(s);
            if(null == balancerLoader) {
                return FinderHelper.firstElement(methodIntercepts);
            }
            return (MethodIntercept) balancerLoader.balancer(methodIntercepts);
        }
        return null;
    }

    /**
     * 获取所有方法代理
     */
    public void getAllInteceptor() {
        return;
    }
}

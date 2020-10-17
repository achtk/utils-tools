package com.chua.utils.tools.function.intercept;

import com.chua.utils.tools.cache.GuavaCacheProvider;
import com.chua.utils.tools.cache.ICacheProvider;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.limit.ILimiterProvider;
import com.chua.utils.tools.limit.TokenLimitProvider;
import com.chua.utils.tools.mapper.ProxyMapper;
import com.chua.utils.tools.strategy.handler.IStrategyPolicy;
import com.google.common.base.Joiner;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 缓存方法拦截器
 * @author CH
 * @version 1.0.0
 * @since 2020/10/12
 */
public class CacheMethodIntercept implements MethodIntercept {

    private static final ICacheProvider<String, Object> provider = new GuavaCacheProvider<>();
    private String[] exclude = ArraysHelper.emptyString();

    public CacheMethodIntercept(String[] exclude) {
        this.exclude = exclude;
    }

    @Override
    public Object invoke(Object obj, Method method, Object[] args, Object proxy) throws Throwable {
        if(ArraysHelper.contains(exclude, method.getName())) {
            if(proxy instanceof MethodProxy) {
                ((MethodProxy) proxy).invokeSuper(obj, args);
            }
            return ProxyMapper.intercept(obj, method, args, proxy);
        }
        String cacheKey = createCacheKey(obj, method, args);
        if(provider.container(cacheKey)) {
            System.out.println("--------------");
            return provider.get(cacheKey);
        }
        Object intercept = null;
        if(proxy instanceof MethodProxy) {
            intercept = ((MethodProxy) proxy).invokeSuper(obj, args);
        } else {
            intercept = ProxyMapper.intercept(obj, method, args, proxy);
        }
        if(null == intercept) {
            return null;
        }
        provider.put(cacheKey, intercept);
        return intercept;
    }

    /**
     * 缓存索引
     * @param obj
     * @param method
     * @param args
     * @return
     */
    private String createCacheKey(Object obj, Method method, Object[] args) {
        return obj.getClass().getName() + "@" +method.getName() + "@" + Joiner.on("-").skipNulls().join(args);
    }
}

package com.chua.utils.tools.strategy;

import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.google.common.base.Joiner;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 缓存策略
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
@NoArgsConstructor
public class CacheStrategy<T> extends StandardProxyStrategy<T> implements Strategy<T>, MethodIntercept<MethodProxy> {

    @Setter
    private int timeout = 0;

    private final CacheProvider<String, Object> cache = new ConcurrentCacheProvider<>();

    public CacheStrategy(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public Object invoke(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String key = getKey(method, args);
        Object present = cache.get(key);
        if (null != present) {
            return present;
        }
        Object invokeSuper = method.invoke(getSource(), args);
        if (null == invokeSuper) {
            return null;
        }
        cache.put(key, invokeSuper);
        return invokeSuper;
    }

    /**
     * 获取索引值
     *
     * @param method 方法名
     * @param args   参数
     * @return 索引值
     */
    private String getKey(Method method, Object[] args) {
        String name = method.getName();
        return name + "$" + Joiner.on(",").join(args);
    }

    @Override
    public T create(T source) {
        super.setMethodIntercept(this);
        return super.proxy(source);
    }
}

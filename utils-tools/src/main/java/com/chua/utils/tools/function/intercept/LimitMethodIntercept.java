package com.chua.utils.tools.function.intercept;

import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.limit.ILimiterProvider;
import com.chua.utils.tools.limit.TokenLimitProvider;
import com.chua.utils.tools.mapper.ProxyMapper;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

/**
 * 限流方法拦截器
 * @author CH
 * @version 1.0.0
 * @since 2020/10/12
 */
public class LimitMethodIntercept implements MethodIntercept {

    private String group;
    private int size;

    private ILimiterProvider limiterProvider = new TokenLimitProvider();
    private String[] exclude = ArraysHelper.emptyString();

    public LimitMethodIntercept(String group, int size) {
        this.group = group;
        this.size = size;
        this.limiterProvider.newLimiter(group, size);
    }

    public LimitMethodIntercept(String group, int size, String[] exclude) {
        this.group = group;
        this.size = size;
        this.exclude = exclude;
        this.limiterProvider.newLimiter(group, size);
    }

    @Override
    public Object invoke(Object obj, Method method, Object[] args, Object... proxy) throws Throwable {
        if(ArraysHelper.contains(exclude, method.getName())) {
            return ProxyMapper.intercept(obj, method, args, proxy);
        }
        if(limiterProvider.tryAcquire(group)) {
          return ProxyMapper.intercept(obj, method, args, proxy);
        }
        return null;
    }
}

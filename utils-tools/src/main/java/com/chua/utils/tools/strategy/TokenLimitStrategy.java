package com.chua.utils.tools.strategy;

import com.chua.utils.tools.exceptions.BusyServiceException;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.google.common.util.concurrent.RateLimiter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 限流策略
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
@NoArgsConstructor
public class TokenLimitStrategy<T> extends StandardProxyStrategy<T> implements Strategy<T>, MethodIntercept<MethodProxy> {

    @Setter
    private double token;

    private RateLimiter rateLimiter;

    public TokenLimitStrategy(double token) {
        this.token = token;
    }

    @Override
    public T create(T source) {
        this.rateLimiter = RateLimiter.create(token);
        super.setMethodIntercept(this);
        return this.proxy(source);
    }

    @Override
    public Object invoke(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (rateLimiter.tryAcquire()) {
            return method.invoke(getSource(), args);
        }
        throw new BusyServiceException();
    }
}

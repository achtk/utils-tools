package com.chua.utils.tools.strategy;

import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.chua.utils.tools.loader.BalancerLoader;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 平衡策略
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
@AllArgsConstructor
@NoArgsConstructor
public class BalanceStrategy<T> extends StandardProxyStrategy<T> implements Strategy<T>, MethodIntercept<MethodProxy> {

    @Setter
    private BalancerLoader<T> balancerLoader;

    @Override
    public Object invoke(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        T balancer = balancerLoader.balancer();
        if (obj.getClass().isAssignableFrom(balancer.getClass())) {
            return method.invoke(balancer, args);
        }
        return proxy.invokeSuper(obj, args);
    }

    @Override
    public T create(T source) {
        super.setMethodIntercept(this);
        return super.proxy(source);
    }
}

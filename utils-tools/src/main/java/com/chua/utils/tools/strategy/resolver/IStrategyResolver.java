package com.chua.utils.tools.strategy.resolver;

import com.chua.utils.tools.config.StrategyProperties;
import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.proxy.CglibProxyAgent;
import com.chua.utils.tools.proxy.ProxyAgent;

/**
 * 策略接口
 * @author CH
 */
public interface IStrategyResolver<T> {
    /**
     * 策略执行
     * @param strategyProperties
     * @return
     */
    T callee(StrategyProperties strategyProperties);

    /**
     * 获取代理
     * @param strategyProperties 策略属性
     * @return
     */
    default public ProxyAgent<T> getProxy(StrategyProperties strategyProperties) {
        ProxyAgent<T> proxyAgent = new CglibProxyAgent<>(methodIntercept(strategyProperties));
        return proxyAgent;
    }

    /**
     * 方法拦截器
     * @param strategyProperties  策略属性
     * @return
     */
    MethodIntercept methodIntercept(StrategyProperties strategyProperties);
}

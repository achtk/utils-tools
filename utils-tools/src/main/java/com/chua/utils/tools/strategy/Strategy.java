package com.chua.utils.tools.strategy;

import com.chua.utils.tools.config.StrategyProperties;
import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.chua.utils.tools.strategy.enums.StrategyType;
import com.chua.utils.tools.strategy.handler.IStrategyPolicy;
import com.chua.utils.tools.strategy.resolver.IStrategyResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略
 * @author CH
 */
@AllArgsConstructor
public class Strategy<T> {
    /**
     * 方法
     */
    private StrategyProperties<T> tStrategyProperties;
    /**
     * 策略
     */
    private StrategyType strategyType;
    /**
     *
     * @return
     */
    public static <T>Strategy.Builder<T> newBuilder(final StrategyType strategyType) {
        return new Strategy.Builder<T>(strategyType);
    }

    /**
     * 执行
     * @return
     */
    public T newProxy() {
        return newProxy(null);
    }
    /**
     * 执行
     * @param classes 类
     * @return
     */
    public T newProxy(final Class<T> classes) {
        if(null == strategyType) {
            throw new NullPointerException("策略不存在");
        }
        if(null == tStrategyProperties) {
            tStrategyProperties = new StrategyProperties<>();
        }
        if(null != classes) {
            tStrategyProperties.setClasses(classes);
        }
        IStrategyResolver strategyResolver = ExtensionFactory.getExtensionLoader(IStrategyResolver.class).getExtension(strategyType.name().toLowerCase());
        if(null == strategyResolver) {
            throw new NullPointerException("["+ strategyType +"]策略不存在");
        }
        return (T) strategyResolver.callee(tStrategyProperties);
    }

    /**
     *
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Builder<T> {
        private final StrategyType strategyType;
        /**
         * 尝试次数
         */
        private int retry = 1;
        /**
         * 时间
         */
        private int timeout = 1000;
        /**
         * 方法
         */
        private IStrategyPolicy<T> strategyPolicy;

        /**
         * 接口
         */
        private Class<?> interfaces;
        /**
         * 方法以及拦截器
         */
        private Map<String, MethodIntercept> methodInterceptMap = new HashMap<>();

        public Builder<T> setInterfaces(Class<?> interfaces) {
            this.interfaces = interfaces;
            return this;
        }

        public Builder<T> appendMethodIntercept(String name, MethodIntercept methodIntercept) {
            methodInterceptMap.put(name, methodIntercept);
            return this;
        }


        public Builder(StrategyType strategyType) {
            this.strategyType = strategyType;
        }


        public Strategy build() {
            StrategyProperties<T> strategyProperties = new StrategyProperties<>();
            strategyProperties.setRetry(retry);
            strategyProperties.setTimeout(timeout);
            strategyProperties.setClasses(interfaces);
            strategyProperties.setMethodInterceptMap(methodInterceptMap);

            return new Strategy(strategyProperties, strategyType);
        }
    }
}

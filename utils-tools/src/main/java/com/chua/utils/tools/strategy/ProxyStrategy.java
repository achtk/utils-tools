package com.chua.utils.tools.strategy;

import com.chua.utils.tools.function.intercept.MethodIntercept;

/**
 * 代理策略
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public interface ProxyStrategy<T, Intercept> {
    /**
     * 设置接口
     *
     * @param interfaces 接口
     */
    void interfaces(Class<?>... interfaces);

    /**
     * 设置类加载器
     *
     * @param classloader 类加载器
     */
    void classloader(ClassLoader classloader);

    /**
     * 方法拦截器
     *
     * @param methodIntercept 方法拦截器
     */
    void intercept(MethodIntercept<Intercept> methodIntercept);

    /**
     * 代理策略
     *
     * @param source 实体对象
     * @return 代理类
     */
    T proxy(T source);
}

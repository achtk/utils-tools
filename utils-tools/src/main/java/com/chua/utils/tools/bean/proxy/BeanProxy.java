package com.chua.utils.tools.bean.proxy;

import com.chua.utils.tools.function.intercept.MethodIntercept;

/**
 * bean代理
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/8
 */
public interface BeanProxy<T> {
    /**
     * 代理
     *
     * @param object 对象
     * @return 代理
     */
    static <T> BeanProxy<T> of(T object) {
        return new StandardBeanProxy<>(object);
    }

    /**
     * 添加适配器
     *
     * @param methodName 方法名
     * @param intercept  拦截器
     * @return this
     */
    BeanProxy<T> addAdaptor(String methodName, MethodIntercept<T> intercept);

    /**
     * 构建对象
     *
     * @return T
     */
    T create();

}

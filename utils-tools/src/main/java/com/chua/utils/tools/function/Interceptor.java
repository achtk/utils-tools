package com.chua.utils.tools.function;

/**
 * 拦截器
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/28
 */
public interface Interceptor {
    /**
     * 拦截
     *
     * @param args 参数
     * @return 数据
     */
    Object intercept(Object... args);
}

package com.chua.utils.tools.function;

/**
 * 日志拦截器
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/28
 */
public interface LogInterceptor extends Interceptor {
    /**
     * 标识符
     *
     * @return 标识符
     */
    String identifier();

}

package com.chua.utils.http.exception;

import com.chua.utils.http.entity.HttpClientResponse;

/**
 * 异常处理
 * @author CHTK
 */
public interface IThrowableHandler {
    /**
     * 抛出异常
     * @param throwable 异常
     * @return
     */
    public HttpClientResponse throwable(Throwable throwable);
}

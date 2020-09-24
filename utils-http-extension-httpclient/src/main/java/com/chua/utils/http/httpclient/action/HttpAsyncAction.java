package com.chua.utils.http.httpclient.action;

/**
 * http异步动作
 * @author CH
 */
public interface HttpAsyncAction<T> {
    /**
     * 完成
     * @param result 结果
     */
    void completed(T result);

    /**
     * 失败
     * @param ex 异常
     */
    void failed(Exception ex);

    /**
     * 关闭
     */
    void cancelled();
}

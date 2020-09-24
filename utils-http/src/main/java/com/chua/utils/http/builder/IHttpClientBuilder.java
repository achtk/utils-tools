package com.chua.utils.http.builder;


import com.chua.utils.http.callback.Callback;
import com.chua.utils.http.entity.HttpClientResponse;

/**
 * Client构造
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:20
 */
public interface IHttpClientBuilder {

    /**
     * 执行
     * @return
     */
    public HttpClientResponse execute();

    /**
     * 执行
     * @param callback 回调
     */
    public void execute(Callback callback);
}

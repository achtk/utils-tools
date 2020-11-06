package com.chua.utils.http.httpclient.build;

import com.chua.utils.http.builder.IHttpClientBuilder;
import com.chua.utils.http.callback.Callback;
import com.chua.utils.http.config.RequestConfig;
import com.chua.utils.http.entity.HttpClientResponse;
import com.chua.utils.http.httpclient.sync.Async;
import com.chua.utils.http.httpclient.sync.Sync;
import lombok.extern.slf4j.Slf4j;

import static com.chua.utils.tools.constant.HttpConstant.*;

/**
 * httpClient builder
 * @author CHTK
 */
@Slf4j
public class HttpClientBuilder implements IHttpClientBuilder {

    private RequestConfig requestConfig;

    private Sync sync;
    private Async async;

    public HttpClientBuilder(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        this.sync = new Sync(requestConfig);
        this.async = new Async(requestConfig);
    }

    /**
     * @return
     */
    @Override
    public HttpClientResponse execute() {
        if(HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            return sync.executeGet();
        } else if(HTTP_METHOD_POST.equals(requestConfig.getMethod())) {
            return sync.executePost();
        } else if(HTTP_METHOD_PUT.equals(requestConfig.getMethod())) {
            return sync.executePut();
        } else if(HTTP_METHOD_DELETE.equals(requestConfig.getMethod())) {
            return sync.executeDelete();
        }
        return null;
    }


    /**
     * @return
     */
    @Override
    public void execute(final Callback callback) {
        if(HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            async.executeGet(callback);
        } else if(HTTP_METHOD_POST.equals(requestConfig.getMethod())) {
            async.executePost(callback);
        } else if(HTTP_METHOD_PUT.equals(requestConfig.getMethod())) {
            async.executePut(callback);
        } else if(HTTP_METHOD_DELETE.equals(requestConfig.getMethod())) {
            async.executeDelete(callback);
        }
    }

}

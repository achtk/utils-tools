package com.chua.utils.http.httpclient.build;

import com.chua.utils.http.httpclient.sync.Async;
import com.chua.utils.http.httpclient.sync.Sync;
import com.chua.utils.tools.http.callback.ResponseCallback;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.entity.ResponseEntity;
import lombok.extern.slf4j.Slf4j;

import static com.chua.utils.tools.constant.HttpConstant.*;

/**
 * httpClient builder
 *
 * @author CHTK
 */
@Slf4j
public class HttpClientBuilder implements com.chua.utils.tools.http.builder.HttpClientBuilder {

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
    public <T> ResponseEntity<T> execute(Class<T> tClass) {
        ResponseEntity responseEntity = null;
        if (HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            responseEntity = sync.executeGet();
        } else if (HTTP_METHOD_POST.equals(requestConfig.getMethod())) {
            responseEntity = sync.executePost();
        } else if (HTTP_METHOD_PUT.equals(requestConfig.getMethod())) {
            responseEntity = sync.executePut();
        } else if (HTTP_METHOD_DELETE.equals(requestConfig.getMethod())) {
            responseEntity = sync.executeDelete();
        }
        return (ResponseEntity<T>) createResponseEntity(responseEntity, tClass);
    }


    /**
     * @return
     */
    @Override
    public <T> void execute(final ResponseCallback callback, Class<T> tClass) {
        ResponseCallback responseCallback = createCallback(callback, tClass);
        if (HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            async.executeGet(responseCallback);
        } else if (HTTP_METHOD_POST.equals(requestConfig.getMethod())) {
            async.executePost(responseCallback);
        } else if (HTTP_METHOD_PUT.equals(requestConfig.getMethod())) {
            async.executePut(responseCallback);
        } else if (HTTP_METHOD_DELETE.equals(requestConfig.getMethod())) {
            async.executeDelete(responseCallback);
        }
    }

}

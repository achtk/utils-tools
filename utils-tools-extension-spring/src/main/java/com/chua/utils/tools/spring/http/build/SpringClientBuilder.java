package com.chua.utils.tools.spring.http.build;


import com.chua.utils.http.builder.IHttpClientBuilder;
import com.chua.utils.http.callback.Callback;
import com.chua.utils.http.config.RequestConfig;
import com.chua.utils.http.entity.HttpClientResponse;
import com.chua.utils.tools.constant.HttpConstant;
import com.chua.utils.tools.spring.helper.SpringClientAsyncHelper;
import com.chua.utils.tools.spring.helper.SpringClientSyncHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * spring http构建
 * @author CH
 */
public class SpringClientBuilder implements IHttpClientBuilder {

    private RequestConfig requestConfig;

    private SpringClientSyncHelper springClientSyncHelper;

    private SpringClientAsyncHelper springClientAsyncHelper;

    public SpringClientBuilder(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        this.springClientSyncHelper = new SpringClientSyncHelper(requestConfig);
        this.springClientAsyncHelper = new SpringClientAsyncHelper(requestConfig);
    }

    @Override
    public HttpClientResponse execute() {
        if(HttpConstant.HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            return springClientSyncHelper.executeGet();
        } else if(HttpConstant.HTTP_METHOD_POST.equals(requestConfig.getMethod())) {
            return springClientSyncHelper.executePost();
        } else if(HttpConstant.HTTP_METHOD_PUT.equals(requestConfig.getMethod())) {
            return springClientSyncHelper.executePut();
        } else if(HttpConstant.HTTP_METHOD_DELETE.equals(requestConfig.getMethod())) {
            return springClientSyncHelper.executeDelete();
        }
        return null;
    }

    @Override
    public void execute(Callback callback) {
        ListenableFuture<ResponseEntity<Object>> future = null;
        if(HttpConstant.HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            future = springClientAsyncHelper.executeGet();
        } else if(HttpConstant.HTTP_METHOD_POST.equals(requestConfig.getMethod())) {
            future = springClientAsyncHelper.executePost();
        } else if(HttpConstant.HTTP_METHOD_PUT.equals(requestConfig.getMethod())) {
            future = springClientAsyncHelper.executePut();
        } else if(HttpConstant.HTTP_METHOD_DELETE.equals(requestConfig.getMethod())) {
            future = springClientAsyncHelper.executeDelete();
        }
        if(null == future) {
            return;
        }
        future.addCallback(new ListenableFutureCallback<ResponseEntity<Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                callback.onFailure(ex);
            }

            @Override
            public void onSuccess(ResponseEntity<Object> result) {
                callback.onResponse(new HttpClientResponse(result.getStatusCodeValue(), result.getBody()));
            }
        });
    }
}

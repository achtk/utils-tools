package com.chua.utils.http.build;

import com.chua.utils.http.builder.IHttpClientBuilder;
import com.chua.utils.http.callback.Callback;
import com.chua.utils.http.config.RequestConfig;
import com.chua.utils.http.sync.Sync;
import com.chua.utils.http.entity.HttpClientResponse;
import com.chua.utils.tools.constant.HttpConstant;
import lombok.AllArgsConstructor;

/**
 * url builder
 *
 * @author CHTK
 */
@AllArgsConstructor
public class UrlClientBuilder implements IHttpClientBuilder {

    private RequestConfig requestConfig;
    private Sync sync;

    public UrlClientBuilder(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        this.sync = new Sync(requestConfig);
    }

    @Override
    public HttpClientResponse execute() {
        if (HttpConstant.HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            return sync.executeGet();
        } else if (HttpConstant.HTTP_METHOD_POST.equals(requestConfig.getMethod())) {
            return sync.executePost();
        } else if (HttpConstant.HTTP_METHOD_PUT.equals(requestConfig.getMethod())) {
            return sync.executePut();
        } else if (HttpConstant.HTTP_METHOD_DELETE.equals(requestConfig.getMethod())) {
            return sync.executeDelete();
        }
        return null;
    }

    @Override
    public void execute(Callback callback) {

    }
}

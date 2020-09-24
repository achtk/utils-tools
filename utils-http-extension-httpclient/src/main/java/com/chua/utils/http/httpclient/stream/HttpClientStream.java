package com.chua.utils.http.httpclient.stream;


import com.chua.utils.http.builder.IHttpClientBuilder;
import com.chua.utils.http.httpclient.build.HttpClientBuilder;

/**
 * http builder
 * @author CHTK
 */
public class HttpClientStream extends com.chua.utils.http.stream.AbstractHttpClientStream {

    public HttpClientStream() {
    }

    public HttpClientStream(String method) {
        requestConfig.setMethod(method);
    }

    /**
     * 构建
     * @return
     */
    @Override
    public IHttpClientBuilder build() {
        if(isNotBlank(requestConfig.getUrl())) {
            return new HttpClientBuilder(requestConfig);
        }
        return null;
    }



}

package com.chua.utils.http.httpclient.factory;


import com.chua.utils.http.http.IHttpClientFactory;
import com.chua.utils.http.httpclient.stream.HttpClientStream;
import com.chua.utils.http.stream.AbstractHttpClientStream;

/**
 * HttpClient实现
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:10
 */
public class HttpClientClientFactory implements IHttpClientFactory {

    @Override
    public AbstractHttpClientStream newGet() {
        return new HttpClientStream("GET");
    }

    @Override
    public AbstractHttpClientStream newPost() {
        return new HttpClientStream("POST");
    }

    @Override
    public AbstractHttpClientStream newPut() {
        return new HttpClientStream("PUT");
    }

    @Override
    public AbstractHttpClientStream newDelete() {
        return new HttpClientStream("DELETE");
    }
}

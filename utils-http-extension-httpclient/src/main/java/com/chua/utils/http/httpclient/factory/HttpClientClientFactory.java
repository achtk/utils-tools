package com.chua.utils.http.httpclient.factory;


import com.chua.utils.http.httpclient.stream.HttpClientStream;
import com.chua.utils.tools.http.http.HttpClientFactory;

/**
 * HttpClient实现
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:10
 */
public class HttpClientClientFactory implements HttpClientFactory {

    @Override
    public HttpClientStream newGet() {
        return new HttpClientStream("GET");
    }

    @Override
    public HttpClientStream newPost() {
        return new HttpClientStream("POST");
    }

    @Override
    public HttpClientStream newPut() {
        return new HttpClientStream("PUT");
    }

    @Override
    public HttpClientStream newDelete() {
        return new HttpClientStream("DELETE");
    }
}

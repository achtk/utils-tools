package com.chua.utils.http.okhttp.factory;


import com.chua.utils.tools.http.http.HttpClientFactory;
import com.chua.utils.http.okhttp.http.OkHttpHelper;
import com.chua.utils.tools.http.stream.HttpClientStream;

/**
 * HttpClient实现
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:10
 */
public class OkHttpClientFactory implements HttpClientFactory {

    @Override
    public HttpClientStream newGet() {
        return OkHttpHelper.newGet();
    }

    @Override
    public HttpClientStream newPost() {
        return OkHttpHelper.newPost();
    }

    @Override
    public HttpClientStream newPut() {
        return OkHttpHelper.newPut();
    }

    @Override
    public HttpClientStream newDelete() {
        return OkHttpHelper.newDelete();
    }
}

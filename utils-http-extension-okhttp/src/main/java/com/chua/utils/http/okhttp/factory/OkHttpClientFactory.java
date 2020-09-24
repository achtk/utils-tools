package com.chua.utils.http.okhttp.factory;


import com.chua.utils.http.http.IHttpClientFactory;
import com.chua.utils.http.okhttp.http.OkHttpHelper;
import com.chua.utils.http.stream.AbstractHttpClientStream;

/**
 * HttpClient实现
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:10
 */
public class OkHttpClientFactory implements IHttpClientFactory {

    @Override
    public AbstractHttpClientStream newGet() {
        return OkHttpHelper.newGet();
    }

    @Override
    public AbstractHttpClientStream newPost() {
        return OkHttpHelper.newPost();
    }

    @Override
    public AbstractHttpClientStream newPut() {
        return OkHttpHelper.newPut();
    }

    @Override
    public AbstractHttpClientStream newDelete() {
        return OkHttpHelper.newDelete();
    }
}

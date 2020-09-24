package com.chua.utils.http.factory;


import com.chua.utils.http.http.IHttpClientFactory;
import com.chua.utils.http.stream.AbstractHttpClientStream;
import com.chua.utils.http.stream.AbstractUrlClientStream;

/**
 * connection 实现
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:10
 */
public class UrlClientFactory implements IHttpClientFactory {

    @Override
    public AbstractHttpClientStream newGet() {
        return new AbstractUrlClientStream("GET");
    }

    @Override
    public AbstractHttpClientStream newPost() {
        return new AbstractUrlClientStream("POST");
    }

    @Override
    public AbstractHttpClientStream newPut() {
        return new AbstractUrlClientStream("PUT");
    }

    @Override
    public AbstractHttpClientStream newDelete() {
        return new AbstractUrlClientStream("DELETE");
    }
}

package com.chua.utils.tools.spring.http.factory;


import com.chua.utils.http.http.IHttpClientFactory;
import com.chua.utils.http.stream.AbstractHttpClientStream;
import com.chua.utils.tools.constant.HttpConstant;
import com.chua.utils.tools.spring.http.stream.SpringClientStream;

/**
 * spring客户端
 * @author CH
 */
public class SpringClientFactory implements IHttpClientFactory {
    @Override
    public AbstractHttpClientStream newGet() {
        return new SpringClientStream(HttpConstant.HTTP_METHOD_GET);
    }

    @Override
    public AbstractHttpClientStream newPost() {
        return new SpringClientStream(HttpConstant.HTTP_METHOD_POST);
    }

    @Override
    public AbstractHttpClientStream newPut() {
        return new SpringClientStream(HttpConstant.HTTP_METHOD_PUT);
    }

    @Override
    public AbstractHttpClientStream newDelete() {
        return new SpringClientStream(HttpConstant.HTTP_METHOD_DELETE);
    }
}

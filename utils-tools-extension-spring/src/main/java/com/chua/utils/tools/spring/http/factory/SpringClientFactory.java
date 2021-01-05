package com.chua.utils.tools.spring.http.factory;


import com.chua.utils.tools.constant.HttpConstant;
import com.chua.utils.tools.http.http.HttpClientFactory;
import com.chua.utils.tools.http.stream.HttpClientStream;
import com.chua.utils.tools.spring.http.stream.RestTemplateClientStream;

/**
 * spring客户端
 * @author CH
 */
public class SpringClientFactory implements HttpClientFactory {
    @Override
    public HttpClientStream newGet() {
        return new RestTemplateClientStream(HttpConstant.HTTP_METHOD_GET);
    }

    @Override
    public HttpClientStream newPost() {
        return new RestTemplateClientStream(HttpConstant.HTTP_METHOD_POST);
    }

    @Override
    public HttpClientStream newPut() {
        return new RestTemplateClientStream(HttpConstant.HTTP_METHOD_PUT);
    }

    @Override
    public HttpClientStream newDelete() {
        return new RestTemplateClientStream(HttpConstant.HTTP_METHOD_DELETE);
    }
}

package com.chua.tools.spring.http;

import com.chua.tools.spring.http.stream.RestTemplateClientStream;
import com.chua.utils.tools.http.http.HttpClientFactory;
import com.chua.utils.tools.http.stream.HttpClientStream;

import static com.chua.utils.tools.constant.HttpConstant.*;

/**
 * spring - rest
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/5
 */
public class RestHttpClientFactory implements HttpClientFactory {
    @Override
    public HttpClientStream newGet() {
        return new RestTemplateClientStream(HTTP_METHOD_GET);
    }

    @Override
    public HttpClientStream newPost() {
        return new RestTemplateClientStream(HTTP_METHOD_POST);
    }

    @Override
    public HttpClientStream newPut() {
        return new RestTemplateClientStream(HTTP_METHOD_PUT);
    }

    @Override
    public HttpClientStream newDelete() {
        return new RestTemplateClientStream(HTTP_METHOD_DELETE);
    }
}

package com.chua.utils.tools.http.factory;


import com.chua.utils.tools.http.http.HttpClientFactory;
import com.chua.utils.tools.http.stream.HttpClientStream;
import com.chua.utils.tools.http.stream.UrlClientStream;

import static com.chua.utils.tools.constant.StringConstant.*;

/**
 * connection 实现
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:10
 */
public class UrlClientFactory implements HttpClientFactory {

    @Override
    public HttpClientStream newGet() {
        return new UrlClientStream(GET);
    }

    @Override
    public HttpClientStream newPost() {
        return new UrlClientStream(POST);
    }

    @Override
    public HttpClientStream newPut() {
        return new UrlClientStream(PUT);
    }

    @Override
    public HttpClientStream newDelete() {
        return new UrlClientStream(DELETE);
    }
}

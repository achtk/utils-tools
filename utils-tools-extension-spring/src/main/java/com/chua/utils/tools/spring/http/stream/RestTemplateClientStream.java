package com.chua.utils.tools.spring.http.stream;


import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.stream.HttpClientStream;
import com.chua.utils.tools.spring.http.build.SpringClientBuilder;

/**
 * spring客户端流操作
 *
 * @author CH
 */
public class RestTemplateClientStream extends HttpClientStream {

    public RestTemplateClientStream(String method) {
        super(method);
    }

    @Override
    public HttpClientBuilder build() {
        return new SpringClientBuilder(getRequestConfig());
    }
}

package com.chua.tools.spring.http.stream;

import com.chua.tools.spring.http.build.FluxClientBuilder;
import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.stream.HttpClientStream;

/**
 * restTemplate
 *
 * @author CH
 * @version 1.0.0
 * @see org.springframework.web.reactive.function.client.WebClient
 * @since 2021/1/5
 */
public class FluxClientStream extends HttpClientStream {

    public FluxClientStream(String method) {
        super(method);
    }

    @Override
    public HttpClientBuilder build() {
        return new FluxClientBuilder(getRequestConfig());
    }
}

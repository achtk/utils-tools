package com.chua.tools.spring.http.stream;

import com.chua.tools.spring.http.build.RestClientBuilder;
import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.stream.HttpClientStream;

/**
 * restTemplate
 *
 * @author CH
 * @version 1.0.0
 * @see org.springframework.web.client.RestTemplate
 * @since 2021/1/5
 */
public class RestTemplateClientStream extends HttpClientStream {

    public RestTemplateClientStream(String method) {
        super(method);
    }

    @Override
    public HttpClientBuilder build() {
        return new RestClientBuilder(getRequestConfig());
    }
}

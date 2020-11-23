package com.chua.utils.tools.spring.http.stream;


import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.stream.HttpClientStream;
import com.chua.utils.tools.spring.http.build.SpringClientBuilder;

/**
 * spring客户端流操作
 * @author CH
 */
public class SpringClientStream extends HttpClientStream {

    public SpringClientStream(String method) {
        requestConfig.setMethod(method);
    }

    @Override
    public HttpClientBuilder build() {
        if(isNotBlank(requestConfig.getUrl())) {
            return new SpringClientBuilder(requestConfig);
        }
        return null;
    }
}

package com.chua.utils.tools.spring.http.stream;


import com.chua.utils.http.builder.IHttpClientBuilder;
import com.chua.utils.http.stream.AbstractHttpClientStream;
import com.chua.utils.tools.spring.http.build.SpringClientBuilder;

/**
 * spring客户端流操作
 * @author CH
 */
public class SpringClientStream extends AbstractHttpClientStream {

    public SpringClientStream(String method) {
        requestConfig.setMethod(method);
    }

    @Override
    public IHttpClientBuilder build() {
        if(isNotBlank(requestConfig.getUrl())) {
            return new SpringClientBuilder(requestConfig);
        }
        return null;
    }
}

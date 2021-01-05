package com.chua.utils.tools.http.stream;


import com.chua.utils.tools.http.build.UrlClientBuilder;
import com.chua.utils.tools.http.builder.HttpClientBuilder;

/**
 * url builder
 *
 * @author CHTK
 */
public class UrlClientStream extends HttpClientStream implements ClientStream {


    public UrlClientStream(String method) {
        super(method);
    }

    @Override
    public HttpClientBuilder build() {
        return new UrlClientBuilder(getRequestConfig());
    }
}

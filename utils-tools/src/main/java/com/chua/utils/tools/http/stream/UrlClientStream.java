package com.chua.utils.tools.http.stream;


import com.chua.utils.tools.http.build.UrlClientBuilder;
import com.chua.utils.tools.http.builder.HttpClientBuilder;

/**
 * url builder
 * @author CHTK
 */
public class UrlClientStream extends HttpClientStream {

    public UrlClientStream() {
    }

    public UrlClientStream(String method) {
        requestConfig.setMethod(method);
    }

    /**
     * 构建
     * @return
     */
    @Override
    public HttpClientBuilder build() {
        if(isNotBlank(requestConfig.getUrl())) {
            return new UrlClientBuilder(requestConfig);
        }
        return null;
    }


}

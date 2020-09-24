package com.chua.utils.http.stream;


import com.chua.utils.http.builder.IHttpClientBuilder;
import com.chua.utils.http.build.UrlClientBuilder;

/**
 * url builder
 * @author CHTK
 */
public class AbstractUrlClientStream extends AbstractHttpClientStream {

    public AbstractUrlClientStream() {
    }

    public AbstractUrlClientStream(String method) {
        requestConfig.setMethod(method);
    }

    /**
     * 构建
     * @return
     */
    @Override
    public IHttpClientBuilder build() {
        if(isNotBlank(requestConfig.getUrl())) {
            return new UrlClientBuilder(requestConfig);
        }
        return null;
    }


}

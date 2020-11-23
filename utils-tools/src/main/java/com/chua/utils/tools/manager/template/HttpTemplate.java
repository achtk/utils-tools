package com.chua.utils.tools.manager.template;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.http.HttpEntity;
import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.entity.ResponseEntity;
import com.chua.utils.tools.http.factory.UrlClientFactory;
import com.chua.utils.tools.http.http.HttpClientFactory;
import com.chua.utils.tools.http.stream.HttpClientStream;
import com.chua.utils.tools.manager.handler.SimpleUriTemplateHandler;
import com.chua.utils.tools.manager.handler.UriTemplateHandler;
import com.chua.utils.tools.manager.operation.HttpOperations;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static com.chua.utils.tools.constant.StringConstant.GET;

/**
 * http模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class HttpTemplate implements HttpOperations {

    @Setter
    private UriTemplateHandler uriTemplateHandler = new SimpleUriTemplateHandler();
    @Setter
    private HttpClientFactory httpClientFactory = new UrlClientFactory();

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws IOException {
        URI uri = uriTemplateHandler.expand(url, uriVariables);
        return (T) execute(uri, GET, new SimpleRequestCallback(responseType), new SimpleResponseExtractor<>(responseType)).getContent();
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws IOException {
        URI uri = uriTemplateHandler.expand(url, uriVariables);
        return execute(uri, GET, new SimpleRequestCallback(responseType), new SimpleResponseExtractor<>(responseType));
    }

    @Override
    public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws IOException {
        URI uri = uriTemplateHandler.expand(url, uriVariables);
        return (T) execute(uri, GET, new SimpleRequestCallback(responseType), new SimpleResponseExtractor<>(responseType)).getContent();
    }

    @Override
    public <T> ResponseEntity<T> execute(URI url, String method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws IOException {
        HttpClientStream httpClientStream = createRequest(url, method);
        if (null != requestCallback) {
            httpClientStream.doWithCallback(requestCallback);
        }
        HttpClientBuilder httpClientBuilder = httpClientStream.build();
        ResponseEntity responseEntity = httpClientBuilder.execute();
        return responseExtractor.convert(responseEntity);
    }

    /**
     * 创建客户端请求
     *
     * @param url    地址
     * @param method 方法
     * @return 客户端
     */
    private HttpClientStream createRequest(URI url, String method) {
        HttpClientStream httpClientStream = httpClientFactory.newMethod(method);
        return httpClientStream.url(url.toString());
    }

    /**
     * 请求处理器
     */
    @AllArgsConstructor
    class SimpleRequestCallback implements RequestCallback {
        private Object params;

        @Override
        public Map<String, String> getHeaders() {
            if (params instanceof HttpEntity) {
                return ((HttpEntity) params).getHeaders();
            }
            return Collections.emptyMap();
        }

        @Override
        public Map<String, Object> getBodyers() {
            if (params instanceof HttpEntity) {
                return ((HttpEntity) params).getBody();
            }
            return Collections.emptyMap();
        }
    }

    /**
     * 响应提取器
     */
    @AllArgsConstructor
    class SimpleResponseExtractor<T> implements ResponseExtractor<T> {
        private Class<T> responseType;

        @Override
        public ResponseEntity<T> convert(ResponseEntity responseEntity) {
            if (null == responseType) {
                return responseEntity;
            }
            responseEntity.setContent(JsonHelper.fromJson(responseEntity.getContent().toString(), responseType));
            return responseEntity;
        }
    }
}

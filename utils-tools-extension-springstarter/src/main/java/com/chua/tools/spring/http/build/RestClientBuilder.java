package com.chua.tools.spring.http.build;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.constant.HttpConstant;
import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.callback.ResponseCallback;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.entity.ResponseEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;

/**
 * rest builder
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/5
 */
@AllArgsConstructor
public class RestClientBuilder implements HttpClientBuilder {

    private RequestConfig requestConfig;

    @Override
    public <T> ResponseEntity<T> execute(Class<T> tClass) {
        RestTemplate restTemplate = renderTemplate(requestConfig);
        org.springframework.http.ResponseEntity<T> exchange = null;

        if (HttpConstant.HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            HttpEntity httpEntity = new HttpEntity(null, packageHeader(requestConfig.getHeaders()));
            exchange = restTemplate.exchange(packageGetUrl(requestConfig), HttpMethod.GET, httpEntity, tClass);
        } else {
            HttpEntity httpEntity = new HttpEntity(packageBody(requestConfig.getBody()), packageHeader(requestConfig.getHeaders()));
            exchange = restTemplate.exchange(requestConfig.getUrl(), HttpMethod.resolve(requestConfig.getMethod()), httpEntity, tClass);
        }
        return new com.chua.utils.tools.http.entity.ResponseEntity(exchange.getStatusCode().value(), exchange.getBody());
    }

    @Override
    public <T> void execute(ResponseCallback responseCallback, Class<T> tClass) {
        AsyncRestTemplate restTemplate = renderAsyncTemplate(requestConfig);
        ListenableFuture<org.springframework.http.ResponseEntity<T>> future = null;

        if (HttpConstant.HTTP_METHOD_GET.equals(requestConfig.getMethod())) {
            HttpEntity httpEntity = new HttpEntity(null, packageHeader(requestConfig.getHeaders()));
            future = restTemplate.exchange(packageGetUrl(requestConfig), HttpMethod.GET, httpEntity, tClass);
        } else {
            HttpEntity httpEntity = new HttpEntity(packageBody(requestConfig.getBody()), packageHeader(requestConfig.getHeaders()));
            future = restTemplate.exchange(requestConfig.getUrl(), HttpMethod.resolve(requestConfig.getMethod()), httpEntity, tClass);
        }

        if (null == future) {
            return;
        }
        future.addCallback(new ListenableFutureCallback<org.springframework.http.ResponseEntity<T>>() {
            @Override
            public void onFailure(Throwable ex) {
                responseCallback.onFailure(ex);
            }

            @Override
            public void onSuccess(org.springframework.http.ResponseEntity<T> result) {
                responseCallback.onResponse(new com.chua.utils.tools.http.entity.ResponseEntity(result.getStatusCodeValue(), result.getBody()));
            }
        });
    }


    /**
     * Description: 封装请求参数
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    private Map<String, Object> packageBody(final Map<String, Object> params) {
        // 创建访问的地址
        Map<String, Object> uriVariables = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        if (params != null) {
            Set<Map.Entry<String, Object>> entrySet = params.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                Object value = entry.getValue();
                uriVariables.put(entry.getKey(), null == value ? "" : value.toString());
            }
        }
        return uriVariables;
    }

    /**
     * 组装Get-Url
     *
     * @param requestConfig
     * @return
     */
    private String packageGetUrl(RequestConfig requestConfig) {
        String url = requestConfig.getUrl();
        Map<String, Object> body = requestConfig.getBody();
        if (!BooleanHelper.hasLength(body)) {
            return url;
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        return url + "?" + sb.substring(1);
    }


    /**
     * Description: 封装请求消息头
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    private HttpHeaders packageHeader(final Multimap<String, String> headers) {
        // 创建访问的地址
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            return httpHeaders;
        }

        Set<String> keySet = headers.keySet();
        for (String key : keySet) {
            httpHeaders.put(key, Lists.newArrayList(headers.get(key)));
        }
        return httpHeaders;
    }


    /**
     * 渲染template
     *
     * @param requestConfig 请求参数
     * @return
     */
    private RestTemplate renderTemplate(RequestConfig requestConfig) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        if (null != requestConfig.getConnectTimeout() && requestConfig.getConnectTimeout() > 0) {
            requestFactory.setConnectTimeout(requestConfig.getConnectTimeout().intValue());
        }

        if (null != requestConfig.getReadTimeout() && requestConfig.getReadTimeout() > 0) {
            requestFactory.setReadTimeout(requestConfig.getReadTimeout().intValue());
        }

        return new RestTemplate(requestFactory);
    }

    /**
     * 渲染template
     *
     * @param requestConfig 请求参数
     * @return
     */
    private AsyncRestTemplate renderAsyncTemplate(RequestConfig requestConfig) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        if (null != requestConfig.getConnectTimeout() && requestConfig.getConnectTimeout() > 0) {
            requestFactory.setConnectTimeout(requestConfig.getConnectTimeout().intValue());
        }

        if (null != requestConfig.getReadTimeout() && requestConfig.getReadTimeout() > 0) {
            requestFactory.setReadTimeout(requestConfig.getReadTimeout().intValue());
        }

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.initialize();
        requestFactory.setTaskExecutor(threadPoolTaskExecutor);
        return new AsyncRestTemplate(requestFactory, renderTemplate(requestConfig));
    }
}

package com.chua.utils.tools.spring.helper;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.http.config.RequestConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;


/**
 * springhttp工具类
 *
 * @author CH
 */
@Slf4j
@SuppressWarnings("all")
public class SpringClientAsyncHelper {

    private RequestConfig requestConfig;
    private AsyncRestTemplate asyncRestTemplate;

    public SpringClientAsyncHelper(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        this.asyncRestTemplate = renderTemplate(requestConfig);
    }


    public SpringClientAsyncHelper(AsyncRestTemplate asyncRestTemplate, RequestConfig requestConfig) {
        this.asyncRestTemplate = asyncRestTemplate;
        this.requestConfig = requestConfig;
    }

    public SpringClientAsyncHelper(AsyncClientHttpRequestFactory requestFactory, RequestConfig requestConfig) {
        this.asyncRestTemplate = new AsyncRestTemplate(requestFactory);
        this.requestConfig = requestConfig;
    }


    /**
     * @return
     */
    public ListenableFuture<ResponseEntity<Object>> executeGet() {
        if (log.isDebugEnabled()) {
            log.debug("请求信息: {}", requestConfig.getUrl());
        }
        HttpEntity httpEntity = new HttpEntity(null, packageHeader(requestConfig.getHeaders()));
        try {
            return asyncRestTemplate.exchange(packageGetUrl(requestConfig), HttpMethod.GET, httpEntity, Object.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 组装Get-Url
     *
     * @param requestConfig
     * @return
     */
    private String packageGetUrl(RequestConfig requestConfig) {
        String url = requestConfig.getUrl();
        Map<String, Object> bodyers = requestConfig.getBody();
        if (!BooleanHelper.hasLength(bodyers)) {
            return url;
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : bodyers.entrySet()) {
            sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        return url + "?" + sb.substring(1);
    }

    /**
     * @return
     */
    public ListenableFuture<ResponseEntity<Object>> executePost() {
        if (log.isDebugEnabled()) {
            log.debug("请求信息: {}", requestConfig.getUrl());
        }
        HttpEntity httpEntity = new HttpEntity(packageBody(requestConfig.getBody()), packageHeader(requestConfig.getHeaders()));
        try {
            return asyncRestTemplate.exchange(requestConfig.getUrl(), HttpMethod.POST, httpEntity, Object.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return
     */
    public ListenableFuture<ResponseEntity<Object>> executePut() {
        if (log.isDebugEnabled()) {
            log.debug("请求信息: {}", requestConfig.getUrl());
        }
        HttpEntity httpEntity = new HttpEntity(packageBody(requestConfig.getBody()), packageHeader(requestConfig.getHeaders()));
        try {
            return asyncRestTemplate.exchange(requestConfig.getUrl(), HttpMethod.PUT, httpEntity, Object.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return
     */
    public ListenableFuture<ResponseEntity<Object>> executeDelete() {
        if (log.isDebugEnabled()) {
            log.debug("请求信息: {}", requestConfig.getUrl());
        }
        HttpEntity httpEntity = new HttpEntity(packageBody(requestConfig.getBody()), packageHeader(requestConfig.getHeaders()));
        try {
            ListenableFuture<ResponseEntity<Object>> exchange = asyncRestTemplate.exchange(requestConfig.getUrl(), HttpMethod.DELETE, httpEntity, Object.class);
            return exchange;
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
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
     * Description: 封装请求消息头
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    private HttpHeaders packageHeader(final Multimap<String, String> params) {
        // 创建访问的地址
        HttpHeaders headers = new HttpHeaders();
        if (params == null) {
            return headers;
        }
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            headers.put(key, Lists.newArrayList(params.get(key)));
        }
        return headers;
    }


    /**
     * 渲染template
     *
     * @param requestConfig 请求参数
     * @return
     */
    private AsyncRestTemplate renderTemplate(RequestConfig requestConfig) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        if (null != requestConfig.getConnectTimeout() && requestConfig.getConnectTimeout() > 0) {
            requestFactory.setConnectTimeout(requestConfig.getConnectTimeout().intValue());
        }

        if (null != requestConfig.getReadTimeout() && requestConfig.getReadTimeout() > 0) {
            requestFactory.setReadTimeout(requestConfig.getReadTimeout().intValue());
        }

        //设置异步任务（线程不会重用，每次调用时都会重新启动一个新的线程）
        requestFactory.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return new AsyncRestTemplate(requestFactory);
    }

}

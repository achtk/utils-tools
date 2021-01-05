package com.chua.utils.tools.spring.helper;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.http.HttpStatus;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
public class SpringClientSyncHelper {

    private RequestConfig requestConfig;
    private RestTemplate restTemplate;

    public SpringClientSyncHelper(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        this.restTemplate = renderTemplate(requestConfig);
    }


    public SpringClientSyncHelper(RestTemplate restTemplate, RequestConfig requestConfig) {
        this.restTemplate = restTemplate;
        this.requestConfig = requestConfig;
    }

    public SpringClientSyncHelper(ClientHttpRequestFactory requestFactory, RequestConfig requestConfig) {
        this.restTemplate = new RestTemplate(requestFactory);
        this.requestConfig = requestConfig;
    }

    /**
     * @return
     */
    public com.chua.utils.tools.http.entity.ResponseEntity executeGet() {
        if (log.isDebugEnabled()) {
            log.debug("请求信息: {}", requestConfig.getUrl());
        }
        HttpEntity httpEntity = new HttpEntity(null, packageHeader(requestConfig.getHeaders()));
        try {
            ResponseEntity<Object> exchange = restTemplate.exchange(packageGetUrl(requestConfig), HttpMethod.GET, httpEntity, Object.class);
            return new com.chua.utils.tools.http.entity.ResponseEntity(exchange.getStatusCode().value(), exchange.getBody());
        } catch (RestClientException e) {
            e.printStackTrace();
            return new com.chua.utils.tools.http.entity.ResponseEntity(HttpStatus.SC_INTERNAL_SERVER_ERROR);
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
     * @return
     */
    public com.chua.utils.tools.http.entity.ResponseEntity executePost() {
        if (log.isDebugEnabled()) {
            log.debug("请求信息: {}", requestConfig.getUrl());
        }
        HttpEntity httpEntity = new HttpEntity(packageBody(requestConfig.getBody()), packageHeader(requestConfig.getHeaders()));
        try {
            ResponseEntity<String> exchange = restTemplate.exchange(requestConfig.getUrl(), HttpMethod.POST, httpEntity, String.class);
            return new com.chua.utils.tools.http.entity.ResponseEntity(exchange.getStatusCode().value(), exchange.getBody());
        } catch (RestClientException e) {
            e.printStackTrace();
            return new com.chua.utils.tools.http.entity.ResponseEntity(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @return
     */
    public com.chua.utils.tools.http.entity.ResponseEntity executePut() {
        if (log.isDebugEnabled()) {
            log.debug("请求信息: {}", requestConfig.getUrl());
        }
        HttpEntity httpEntity = new HttpEntity(packageBody(requestConfig.getBody()), packageHeader(requestConfig.getHeaders()));
        try {
            ResponseEntity<String> exchange = restTemplate.exchange(requestConfig.getUrl(), HttpMethod.PUT, httpEntity, String.class);
            return new com.chua.utils.tools.http.entity.ResponseEntity(exchange.getStatusCode().value(), exchange.getBody());
        } catch (RestClientException e) {
            e.printStackTrace();
            return new com.chua.utils.tools.http.entity.ResponseEntity(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @return
     */
    public com.chua.utils.tools.http.entity.ResponseEntity executeDelete() {
        if (log.isDebugEnabled()) {
            log.debug("请求信息: {}", requestConfig.getUrl());
        }
        HttpEntity httpEntity = new HttpEntity(packageBody(requestConfig.getBody()), packageHeader(requestConfig.getHeaders()));
        try {
            ResponseEntity<String> exchange = restTemplate.exchange(requestConfig.getUrl(), HttpMethod.DELETE, httpEntity, String.class);
            return new com.chua.utils.tools.http.entity.ResponseEntity(exchange.getStatusCode().value(), exchange.getBody());
        } catch (RestClientException e) {
            e.printStackTrace();
            return new com.chua.utils.tools.http.entity.ResponseEntity(HttpStatus.SC_INTERNAL_SERVER_ERROR);
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
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        if (null != requestConfig.getConnectTimeout() && requestConfig.getConnectTimeout() > 0) {
            requestFactory.setConnectTimeout(requestConfig.getConnectTimeout().intValue());
        }

        if (null != requestConfig.getReadTimeout() && requestConfig.getReadTimeout() > 0) {
            requestFactory.setReadTimeout(requestConfig.getReadTimeout().intValue());
        }

        return new RestTemplate(requestFactory);
    }

}

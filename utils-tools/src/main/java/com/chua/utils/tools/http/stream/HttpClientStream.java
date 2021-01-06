package com.chua.utils.tools.http.stream;


import com.chua.utils.tools.common.HttpClientHelper;
import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.exception.ThrowableHandler;
import com.chua.utils.tools.manager.operation.HttpOperations;
import lombok.Getter;

import javax.net.ssl.SSLContext;
import java.util.Map;

/**
 * HttpClient流式操作
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:11
 */
public class HttpClientStream implements ClientStream {

    @Getter
    private RequestConfig requestConfig = new RequestConfig();

    public HttpClientStream(String method) {
        this.method(method);
    }

    /**
     * 请求方法
     *
     * @param method 方法
     * @return this
     */
    public HttpClientStream method(final String method) {
        requestConfig.setMethod(method);
        return this;
    }

    /**
     * 添加消息头
     *
     * @param headerName  消息头
     * @param headerValue 消息内容
     * @return this
     */
    public HttpClientStream header(final String headerName, final String headerValue) {
        requestConfig.getHeaders().put(headerName, headerValue);
        return this;
    }

    /**
     * 添加消息头
     *
     * @param header 消息头
     * @return this
     */
    public HttpClientStream header(final Map<String, String> header) {
        header.entrySet().forEach(entry -> {
            header(entry.getKey(), entry.getValue());
        });
        return this;
    }

    /**
     * 添加表单信息
     *
     * @param formName  消息头
     * @param formValue 消息内容
     * @return this
     */
    public HttpClientStream body(final String formName, final Object formValue) {
        requestConfig.getBody().put(formName, formValue);
        return this;
    }

    /**
     * 添加表单信息
     *
     * @param formValues 表单信息
     * @return this
     */
    public HttpClientStream body(final Map<String, Object> formValues) {
        formValues.entrySet().forEach(entry -> {
            body(entry.getKey(), entry.getValue());
        });
        return this;
    }

    /**
     * 添加表单信息
     *
     * @param cookieName  消息头
     * @param cookieValue 消息内容
     * @return this
     */
    public HttpClientStream cookie(final String cookieName, final String cookieValue) {
        requestConfig.getCookie().put(cookieName, cookieValue);
        return this;
    }

    /**
     * 添加表单信息
     *
     * @param cookieValues 表单信息
     * @return this
     */
    public HttpClientStream cookie(final Map<String, String> cookieValues) {
        cookieValues.entrySet().forEach(entry -> {
            cookie(entry.getKey(), entry.getValue());
        });
        return this;
    }


    /**
     * sslContext
     *
     * @param sslContext sslContext
     * @return this
     */
    public HttpClientStream sslContext(final SSLContext sslContext) {
        return this;
    }

    /**
     * 请求地址
     *
     * @param url 请求地址
     * @return this
     */
    public HttpClientStream url(String url) {
        this.requestConfig.setUrl(url);
        return this;
    }

    /**
     * 超时时间
     *
     * @param timeout 超时时间
     * @return this
     */
    public HttpClientStream timeout(long timeout) {
        this.requestConfig.setTimeout(timeout);
        return this;
    }

    /**
     * 请求回调
     *
     * @param requestCallback 请求回调
     */
    public void doWithCallback(HttpOperations.RequestCallback requestCallback) {
        Map<String, String> headers = requestCallback.getHeaders();
        this.header(headers);

        Object body = requestCallback.getBodyers();
        if (body instanceof Map) {
            this.body((Map<String, Object>) body);
        }
    }

    /**
     * 转化为https
     *
     * @return
     */
    public HttpClientStream https() {
        requestConfig.setHttps(true);
        return this;
    }

    /**
     * 连接时间
     *
     * @param connectTimeout
     * @return
     */
    public HttpClientStream connectTimeout(final long connectTimeout) {
        requestConfig.setConnectTimeout(connectTimeout);
        return this;
    }

    /**
     * 重试
     *
     * @param retry
     * @return
     */
    public HttpClientStream retry(final int retry) {
        requestConfig.setRetry(retry);
        return this;
    }

    /**
     * 读取时间
     *
     * @param readTimeout
     * @return
     */
    public HttpClientStream readTimeout(final long readTimeout) {
        requestConfig.setReadTimeout(readTimeout);
        return this;
    }

    /**
     * 读取时间
     *
     * @param socketTimeout
     * @return
     */
    public HttpClientStream socketTimeout(final long socketTimeout) {
        requestConfig.setSocketTimeout(socketTimeout);
        return this;
    }

    /**
     * 异常
     *
     * @param throwableHandler
     * @return
     */
    public HttpClientStream throwable(ThrowableHandler throwableHandler) {
        requestConfig.setHandler(throwableHandler);
        return this;
    }

    /**
     * ssl
     *
     * @return
     */
    public HttpClientStream sslSocketFactory(final Object sslSocketFactory) {
        requestConfig.setSslSocketFactory(null == sslSocketFactory ? HttpClientHelper.createSslSocketFactory() : sslSocketFactory);
        return this;
    }


    @Override
    public HttpClientBuilder build() {
        return null;
    }
}

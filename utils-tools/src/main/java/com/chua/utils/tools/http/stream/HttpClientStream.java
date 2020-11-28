package com.chua.utils.tools.http.stream;


import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.http.builder.HttpClientBuilder;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.exception.ThrowableHandler;
import com.chua.utils.tools.http.meta.MetaType;
import com.chua.utils.tools.common.HttpClientHelper;
import com.chua.utils.tools.manager.operation.HttpOperations;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpClient流式操作
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:11
 */
public class HttpClientStream implements HttpClientStreamBuilder {

    protected Map<String, String> headers = new HashMap<>();
    protected Map<String, Object> bodyers = new HashMap<>();
    protected Map<String, InputStream> streams = new HashMap<>();
    protected RequestConfig requestConfig = new RequestConfig();

    {
        requestConfig.setHeaders(headers);
        requestConfig.setBodyers(bodyers);
        requestConfig.setStreams(streams);
    }

    /**
     * 设置 url
     *
     * @param url
     * @return
     */
    public HttpClientStream url(String url) {
        requestConfig.setUrl(url);
        return this;
    }

    /**
     * 添加消息头部
     *
     * @param headerName
     * @param headerValue
     * @return
     */
    public HttpClientStream addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
        return this;
    }

    /**
     * 添加流
     *
     * @param name
     * @param is
     * @return
     */
    public HttpClientStream addStream(String name, InputStream is) {
        streams.put(name, is);
        return this;
    }

    /**
     * 添加消息内容
     *
     * @param params
     * @return
     */
    public HttpClientStream setHeader(Map<String, String> params) {
        this.headers = params;
        return this;
    }

    /**
     * 添加消息内容
     *
     * @param bodyName
     * @param bodyValue
     * @return
     */
    public HttpClientStream addBody(String bodyName, Object bodyValue) {
        bodyers.put(bodyName, bodyValue);
        return this;
    }

    /**
     * 添加消息内容
     *
     * @param params
     * @return
     */
    public HttpClientStream setBody(Map<String, Object> params) {
        this.bodyers = params;
        return this;
    }

    /**
     * 添加消息内容
     *
     * @param json
     * @return
     */
    public HttpClientStream addJson(String json) {
        requestConfig.setText(json, MetaType.APPLICATION_JSON);
        return this;
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
     * 添加文本
     *
     * @param json
     * @param metaType
     * @return
     */
    public HttpClientStream addText(final String json, final MetaType metaType) {
        if (isNotBlank(json)) {
            requestConfig.setText(json, metaType);
        }
        return this;
    }

    /**
     * 添加消息内容
     *
     * @param script
     * @return
     */
    public HttpClientStream addScript(final String script) {
        if (isNotBlank(script)) {
            requestConfig.setText(script, MetaType.APPLICATION_JAVASCRIPT);
        }
        return this;
    }

    /**
     * 添加消息内容
     *
     * @param html
     * @return
     */
    public HttpClientStream addHtml(String html) {
        if (isNotBlank(html)) {
            requestConfig.setText(html, MetaType.TEXT_HTML);
        }
        return this;
    }

    /**
     * 添加消息内容
     *
     * @param script
     * @return
     */
    public HttpClientStream addXml(String script) {
        if (isNotBlank(script)) {
            requestConfig.setText(script, MetaType.TEXT_XML);
        }
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

    /**
     * 是否为空
     *
     * @param headerName
     * @return
     */
    protected boolean isNotBlank(String headerName) {
        return null != headerName && !"".equals(headerName);
    }

    /**
     * 是否为空
     *
     * @param params
     * @return
     */
    protected <T> boolean isNotBlank(Map<String, T> params) {
        return null != params && !params.isEmpty();
    }

    /**
     * 组装信息
     *
     * @param requestCallback 请求回调
     */
    public void doWithCallback(HttpOperations.RequestCallback requestCallback) {
        Map<String, String> headers = requestCallback.getHeaders();
        this.setHeader(headers);

        Object bodyers = requestCallback.getBodyers();
        if (bodyers instanceof Map) {
            this.setBody((Map<String, Object>) bodyers);
        } else if (bodyers instanceof String) {
            this.addJson((String) bodyers);
        } else {
            this.addJson(JsonHelper.toJson(bodyers));
        }

    }

    @Override
    public HttpClientBuilder build() {
        return null;
    }
}

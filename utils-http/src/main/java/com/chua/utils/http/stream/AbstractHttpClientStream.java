package com.chua.utils.http.stream;


import com.chua.utils.http.builder.IHttpClientBuilder;
import com.chua.utils.http.config.RequestConfig;
import com.chua.utils.http.exception.IThrowableHandler;
import com.chua.utils.http.meta.MetaType;
import com.chua.utils.tools.common.HttpClientHelper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpClient流式操作
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:11
 */
public abstract class AbstractHttpClientStream {

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
     * @param url
     * @return
     */
    public AbstractHttpClientStream url(String url) {
        requestConfig.setUrl(url);
        return this;
    }

    /**
     * 添加消息头部
     * @param headerName
     * @param headerValue
     * @return
     */
    public AbstractHttpClientStream addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
        return this;
    }

    /**
     * 添加流
     * @param name
     * @param is
     * @return
     */
    public AbstractHttpClientStream addStream(String name, InputStream is) {
        streams.put(name, is);
        return this;
    }

    /**
     * 添加消息内容
     * @param params
     * @return
     */
    public AbstractHttpClientStream setHeader(Map<String, String> params) {
        this.headers = params;
        return this;
    }

    /**
     * 添加消息内容
     * @param bodyName
     * @param bodyValue
     * @return
     */
    public AbstractHttpClientStream addBody(String bodyName, Object bodyValue) {
        bodyers.put(bodyName, bodyValue);
        return this;
    }

    /**
     * 添加消息内容
     * @param params
     * @return
     */
    public AbstractHttpClientStream setBody(Map<String, Object> params) {
        this.bodyers = params;
        return this;
    }

    /**
     * 添加消息内容
     * @param json
     * @return
     */
    public AbstractHttpClientStream addJson(String json) {
        requestConfig.setText(json, MetaType.APPLICATION_JSON);
        return this;
    }

    /**
     * 转化为https
     * @return
     */
    public AbstractHttpClientStream https() {
        requestConfig.setHttps(true);
        return this;
    }


    /**
     * 添加文本
     * @param json
     * @param metaType
     * @return
     */
    public AbstractHttpClientStream addText(final String json, final MetaType metaType) {
        if(isNotBlank(json)) {
            requestConfig.setText(json, metaType);
        }
        return this;
    }
    /**
     * 添加消息内容
     * @param script
     * @return
     */
    public AbstractHttpClientStream addScript(final String script) {
        if(isNotBlank(script)) {
            requestConfig.setText(script, MetaType.APPLICATION_JAVASCRIPT);
        }
        return this;
    }
    /**
     * 添加消息内容
     * @param html
     * @return
     */
    public AbstractHttpClientStream addHtml(String html) {
        if(isNotBlank(html)) {
            requestConfig.setText(html, MetaType.TEXT_HTML);
        }
        return this;
    }
    /**
     * 添加消息内容
     * @param script
     * @return
     */
    public AbstractHttpClientStream addXml(String script) {
        if(isNotBlank(script)) {
            requestConfig.setText(script, MetaType.TEXT_XML);
        }
        return this;
    }

    /**
     * 连接时间
     * @param connectTimeout
     * @return
     */
    public AbstractHttpClientStream connectTimeout(final long connectTimeout) {
        requestConfig.setConnectTimeout(connectTimeout);
        return this;
    }

    /**
     * 重试
     * @param retry
     * @return
     */
    public AbstractHttpClientStream retry(final int retry) {
        requestConfig.setRetry(retry);
        return this;
    }

    /**
     * 读取时间
     * @param readTimeout
     * @return
     */
    public AbstractHttpClientStream readTimeout(final long readTimeout) {
        requestConfig.setReadTimeout(readTimeout);
        return this;
    }

    /**
     * 读取时间
     * @param socketTimeout
     * @return
     */
    public AbstractHttpClientStream socketTimeout(final long socketTimeout) {
        requestConfig.setSocketTimeout(socketTimeout);
        return this;
    }
    /**
     * 异常
     * @param iThrowableHandler
     * @return
     */
    public AbstractHttpClientStream throwable(IThrowableHandler iThrowableHandler) {
        requestConfig.setHandler(iThrowableHandler);
        return this;
    }
    /**
     * ssl
     * @return
     */
    public AbstractHttpClientStream sslSocketFactory(final Object sslSocketFactory) {
        requestConfig.setSslSocketFactory(null == sslSocketFactory ? HttpClientHelper.createSslSocketFactory() : sslSocketFactory);
        return this;
    }    /**
     * 构建
     * @return
     */
    public abstract IHttpClientBuilder build();

    /**
     * 是否为空
     * @param headerName
     * @return
     */
    protected boolean isNotBlank(String headerName) {
        return null != headerName && !"".equals(headerName);
    }
    /**
     * 是否为空
     * @param params
     * @return
     */
    protected <T>boolean isNotBlank(Map<String, T> params) {
        return null != params && !params.isEmpty();
    }


}

package com.chua.utils.http.httpclient.stream;


import com.chua.utils.http.builder.IHttpClientBuilder;
import com.chua.utils.http.exception.IThrowableHandler;
import com.chua.utils.http.httpclient.build.HttpClientBuilder;
import com.chua.utils.http.meta.MetaType;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * http builder
 * @author CHTK
 */
public class AbstractHttpClientStream extends com.chua.utils.http.stream.AbstractHttpClientStream {

    public AbstractHttpClientStream() {
    }

    public AbstractHttpClientStream(String method) {
        requestConfig.setMethod(method);
    }

    /**
     * 设置 url
     * @param url
     * @return
     */
    @Override
    public AbstractHttpClientStream url(final String url) {
        requestConfig.setUrl(url);
        return this;
    }

    /**
     * 添加消息头部
     * @param headerName
     * @param headerValue
     * @return
     */
    @Override
    public AbstractHttpClientStream addHeader(final String headerName, final String headerValue) {
        if(isNotBlank(headerName)) {
            headers.put(headerName, headerValue);
        }
        return this;
    }

    /**
     * 添加消息内容
     * @param params
     * @return
     */
    @Override
    public AbstractHttpClientStream setHeader(final Map<String, String> params) {
        if(isNotBlank(params)) {
            headers.putAll(params);
        }
        return this;
    }

    /**
     * 添加消息内容
     * @param bodyName
     * @param bodyValue
     * @return
     */
    @Override
    public AbstractHttpClientStream addBody(final String bodyName, final Object bodyValue) {
        if(isNotBlank(bodyName)) {
            bodyers.put(bodyName, bodyValue);
        }
        return this;
    }

    /**
     * 添加消息内容
     * @param params
     * @return
     */
    @Override
    public AbstractHttpClientStream setBody(final Map<String, Object> params) {
        if(isNotBlank(params)) {
            bodyers.putAll(params);
        }
        return this;
    }

    /**
     * 添加消息内容
     * @param json
     * @return
     */
    @Override
    public AbstractHttpClientStream addJson(final String json) {
        if(isNotBlank(json)) {
            requestConfig.setText(json, MetaType.APPLICATION_JSON);
        }
        return this;
    }
    /**
     * 添加消息内容
     * @param json
     * @return
     */
    @Override
    public AbstractHttpClientStream addText(final String json, final MetaType metaType) {
        if(isNotBlank(json)) {
            requestConfig.setText(json, metaType);
        }
        return this;
    }

    @Override
    public AbstractHttpClientStream addScript(String script) {
        if(isNotBlank(script)) {
            requestConfig.setText(script, MetaType.APPLICATION_JAVASCRIPT);
        }
        return this;
    }
    @Override
    public AbstractHttpClientStream addHtml(String script) {
        if(isNotBlank(script)) {
            requestConfig.setText(script, MetaType.TEXT_HTML);
        }
        return this;
    }
    @Override
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
    @Override
    public AbstractHttpClientStream connectTimeout(final long connectTimeout) {
        requestConfig.setConnectTimeout(connectTimeout);
        return this;
    }


    /**
     * 重试
     * @param retry
     * @return
     */
    @Override
    public AbstractHttpClientStream retry(final int retry) {
        requestConfig.setRetry(retry);
        return this;
    }
    /**
     * 读取时间
     * @param readTimeout
     * @return
     */
    @Override
    public AbstractHttpClientStream readTimeout(final long readTimeout) {
        requestConfig.setReadTimeout(readTimeout);
        return this;
    }
    /**
     * 读取时间
     * @param socketTimeout
     * @return
     */
    @Override
    public AbstractHttpClientStream socketTimeout(final long socketTimeout) {
        requestConfig.setSocketTimeout(socketTimeout);
        return this;
    }

    @Override
    public AbstractHttpClientStream throwable(IThrowableHandler iThrowableHandler) {
        requestConfig.setHandler(iThrowableHandler);
        return this;
    }

    /**
     * 构建
     * @return
     */
    @Override
    public IHttpClientBuilder build() {
        if(isNotBlank(requestConfig.getUrl())) {
            requestConfig.setHeaders(headers);
            requestConfig.setBodyers(bodyers);
            return new HttpClientBuilder(requestConfig);
        }
        return null;
    }

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     * @return
     */
    public SSLSocketFactory createSslSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }



    /**
     * 用于信任所有证书
     */
    public class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }
        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }


}

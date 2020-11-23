package com.chua.utils.http.httpclient.handler;

import com.chua.utils.http.httpclient.action.HttpAsyncAction;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.charset.CharsetHelper;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.entity.ResponseEntity;
import com.chua.utils.tools.http.meta.MetaType;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.chua.utils.tools.constant.HttpConstant.HTTP_HEADER_CONTENT_TYPE;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

/**
 * HttpClient工具类
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 15:39
 */
@Slf4j
public class HttpClientHandler {

    /**
     * 编码格式。发送编码格式统一用UTF-8
     */
    private final String ENCODING = CharsetHelper.UTF_8;

    protected RequestConfig requestConfig;

    public HttpClientHandler(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }
    /**
     * 设置请求参数
     *
     * @return
     */
    protected org.apache.http.client.config.RequestConfig setRequestConfig() {
        org.apache.http.client.config.RequestConfig.Builder requestBuilder = org.apache.http.client.config.RequestConfig.custom();
        if (requestConfig.getConnectTimeout() > 0) {
            requestBuilder.setConnectTimeout(requestConfig.getConnectTimeout().intValue());
        }
        if (requestConfig.getSocketTimeout() > 0) {
            requestBuilder.setSocketTimeout(requestConfig.getSocketTimeout().intValue());
        }
        requestBuilder.setAuthenticationEnabled(requestConfig.isAuthenticationEnabled());
        requestBuilder.setContentCompressionEnabled(requestConfig.isContentCompressionEnabled());

        return requestBuilder.build();
    }

    /**
     * 获取客户端
     * @return
     */
    protected CloseableHttpClient getClient() {
        CloseableHttpClient client = (CloseableHttpClient) requestConfig.getClient();
        return null == client ? getCustomClient() : client;
    }
    /**
     * 获取客户端
     * @return
     */
    protected CloseableHttpAsyncClient getAsyncClient() {
        CloseableHttpAsyncClient client = (CloseableHttpAsyncClient) requestConfig.getClient();
        return null == client ? getCustomAsyncClient() : client;
    }
    /**
     * 获取客户端
     * @return
     */
    private CloseableHttpAsyncClient getCustomAsyncClient() {
        HttpAsyncClientBuilder httpAsyncClientBuilder = HttpAsyncClients.custom();

        //对照UA字串的标准格式理解一下每部分的意思
        httpAsyncClientBuilder.setUserAgent("Mozilla/5.0(Windows;U;Windows NT 5.1;en-US;rv:0.9.4)");

        if(requestConfig.getMaxConnTotal() > 0) {
            httpAsyncClientBuilder.setMaxConnTotal(requestConfig.getMaxConnTotal());
        }
        if(requestConfig.getMaxConnRoute() > 0) {
            httpAsyncClientBuilder.setMaxConnPerRoute(requestConfig.getMaxConnRoute());
        }

        if(!requestConfig.isHttps()) {
            return httpAsyncClientBuilder.build();
        }
        return httpAsyncClientBuilder.setSSLContext(getSslContext()).build();
    }
    /**
     * 获取客户端
     * @return
     */
    private CloseableHttpClient getCustomClient() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        //对照UA字串的标准格式理解一下每部分的意思
        httpClientBuilder.setUserAgent("Mozilla/5.0(Windows;U;Windows NT 5.1;en-US;rv:0.9.4)");

        if(requestConfig.getRetry() > 0) {
            httpClientBuilder.setRetryHandler(new StandardHttpRequestRetryHandler(requestConfig.getRetry(), false));
        }

        if(!Strings.isNullOrEmpty(requestConfig.getDns())) {
            httpClientBuilder.setDnsResolver(new DnsResolver() {
                @Override
                public InetAddress[] resolve(String host) throws UnknownHostException {
                    return InetAddress.getAllByName(requestConfig.getDns());
                }
            });
        }

        if(requestConfig.getMaxConnTotal() > 0) {
            httpClientBuilder.setMaxConnTotal(requestConfig.getMaxConnTotal());
        }
        if(requestConfig.getMaxConnRoute() > 0) {
            httpClientBuilder.setMaxConnPerRoute(requestConfig.getMaxConnRoute());
        }

        if(!requestConfig.isHttps()) {
            return httpClientBuilder.build();
        }
        return httpClientBuilder.setSSLSocketFactory(getSslSocketFactory()).build();
    }

    /**
     * 获取sslContext
     * @return
     */
    private SSLConnectionSocketFactory getSslSocketFactory() {
        if(requestConfig.isHttps()) {
            if(null == requestConfig.getSslSocketFactory()) {
                try {
                    SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                        @Override
                        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            return true;
                        }
                    }).build();

                    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
                    return socketFactory;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                }
            } else {
                Object sslSocketFactory = requestConfig.getSslSocketFactory();
                return sslSocketFactory instanceof SSLConnectionSocketFactory ? (SSLConnectionSocketFactory) sslSocketFactory : null;
            }
        }
        return null;
    }
    /**
     * 获取sslContext
     * @return
     */
    private SSLContext getSslContext() {
        if(requestConfig.isHttps()) {
            if(null == requestConfig.getSslSocketFactory()) {
                try {
                    return new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                        @Override
                        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            return true;
                        }
                    }).build();

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                }
            } else {
                Object sslSocketFactory = requestConfig.getSslSocketFactory();
                return sslSocketFactory instanceof SSLContext ? (SSLContext) sslSocketFactory : null;
            }
        }
        return null;
    }

    /**
     * Description: 获得响应结果
     *
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public void getHttpAsyncClientResult(final CloseableHttpAsyncClient httpClient, final HttpRequestBase httpMethod, final HttpAsyncAction action) throws Exception {
        // 执行请求
        httpClient.execute(httpMethod, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                action.completed(httpResponse);
            }

            @Override
            public void failed(Exception ex) {
                action.failed(ex);
            }

            @Override
            public void cancelled() {
                action.cancelled();
            }
        });
    }

    /**
     * Description: 释放资源
     *
     * @param httpResponse
     * @param httpAsyncClient
     * @throws IOException
     */
    public void release(CloseableHttpResponse httpResponse, CloseableHttpAsyncClient httpAsyncClient) {
        // 释放资源
        if (httpResponse != null) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (httpAsyncClient != null) {
            try {
                httpAsyncClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Description: 获得响应结果
     *
     * @param httpResponse
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public ResponseEntity getHttpClientResult(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        if(log.isDebugEnabled()) {
            log.debug("请求信息: {}", httpMethod);
        }
        // 执行请求
        try {
            httpResponse = httpClient.execute(httpMethod);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取返回结果
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = "";
            if (httpResponse.getEntity() != null) {
                content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
            }
            if(log.isDebugEnabled()) {
                log.debug("响应状态码: {}", httpResponse.getStatusLine().getStatusCode());
                log.debug("响应数据是否为空: {}", null != content && !content.isEmpty());
            }
            if(log.isTraceEnabled()) {
                log.trace("响应数据: {}", content);
            }
            return new ResponseEntity(httpResponse.getStatusLine().getStatusCode(), content);
        }
        if(log.isDebugEnabled()) {
            log.debug("响应数据异常");
        }
        return new ResponseEntity(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * Description: 释放资源
     *
     * @param httpResponse
     * @param httpClient
     * @throws IOException
     */
    public void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
        // 释放资源
        if (null != httpResponse) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Description: 封装请求参数
     *
     * @param params
     * @param multipartEntityBuilder
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    public void packageParam(Map<String, Object> params, MultipartEntityBuilder multipartEntityBuilder, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, Object>> entrySet = params.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                Object value = entry.getValue();
                nvps.add(new BasicNameValuePair(entry.getKey(), null == value ? "" : value.toString()));
            }
            if(null == multipartEntityBuilder) {
                // 设置到请求的http对象中
                httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
                return;
            }
            for (Map.Entry<String, Object> entry : entrySet) {
                Object value = entry.getValue();
                multipartEntityBuilder.addTextBody(entry.getKey(), null == value ? "" : value.toString());
            }
            httpMethod.setEntity(multipartEntityBuilder.build());
        }
    }
    /**
     * Description: 封装请求参数
     *
     * @param url
     * @throws UnsupportedEncodingException
     * @return
     */
    public URIBuilder packageUriBuilder(final String url, final Map<String, Object> params) throws URISyntaxException {
        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, Object>> entrySet = params.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                Object value = entry.getValue();
                uriBuilder.setParameter(entry.getKey(), null == value ? "" : value.toString());
            }
        }
        return uriBuilder;
    }

    /**
     * Description: 封装请求头
     * @param headers
     * @param httpMethod
     */
    public void packageHeader(Map<String, String> headers, HttpRequestBase httpMethod) {
        // 封装请求头
        if (headers != null) {
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 设置实体
     * @param httpEntityEnclosingRequestBase
     * @return
     */
    protected MultipartEntityBuilder resetEntityParams(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
        Map<String, InputStream> streams = requestConfig.getStreams();
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        for (Map.Entry<String, InputStream> entry : streams.entrySet()) {
            multipartEntityBuilder.addBinaryBody(entry.getKey(), entry.getValue());
        }

        return multipartEntityBuilder;

    }


    /**
     * 设置json数据
     * @param httpEntityEnclosingRequestBase
     */
    protected void resetJsonParams(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
        if(!httpEntityEnclosingRequestBase.containsHeader(CONTENT_TYPE)) {
            httpEntityEnclosingRequestBase.addHeader(CONTENT_TYPE, "application/json;charset=utf-8");
        } else {
            boolean hasJsonHeader = false;
            Header[] headers = httpEntityEnclosingRequestBase.getHeaders(CONTENT_TYPE);
            for (Header header : headers) {
                if("application/json".equalsIgnoreCase(header.getValue())) {
                    hasJsonHeader = true;
                    break;
                }
            }
            if(hasJsonHeader) {
                httpEntityEnclosingRequestBase.addHeader(CONTENT_TYPE, "application/json;charset=utf-8");
            }
        }
        Object text = requestConfig.getText();
        if(text instanceof String) {
            httpEntityEnclosingRequestBase.setEntity(new StringEntity((String) text, "utf-8"));
            return;
        }
        httpEntityEnclosingRequestBase.setEntity(new StringEntity(JsonHelper.toJson(text), "utf-8"));
    }

    /**
     * application/json请求
     * @return
     */
    protected boolean isJson() {
        MetaType metaType = requestConfig.getMetaType();
        return metaType == MetaType.APPLICATION_JSON;
    }
    /**
     *
     * @return
     */
    protected boolean isStream() {
        return requestConfig.getStreams().size() > 0;
    }

    /**
     *
     * @param metaType
     * @return
     */
    protected ContentType metaTypeToContentType(MetaType metaType) {
       return ContentType.create(metaType.getValue());
    }

    /**
     * 设置消息体
     * @param bodyes
     * @param httpEntityEnclosingRequestBase
     */
    public void resetParams(Map<String, Object> bodyes, HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
        MultipartEntityBuilder multipartEntityBuilder = null;
        if(isStream()) {
            multipartEntityBuilder = resetEntityParams(httpEntityEnclosingRequestBase);
        }
        if(isJson()) {
            resetJsonParams(httpEntityEnclosingRequestBase);
            return ;
        }

        //表单方式
        if (null != bodyes && !bodyes.isEmpty()) {
            // 封装请求参数
            try {
                packageParam(bodyes, multipartEntityBuilder, httpEntityEnclosingRequestBase);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            Object text = requestConfig.getText();
            MetaType metaType = requestConfig.getMetaType();
            String value = metaType.getValue();
            if(!httpEntityEnclosingRequestBase.containsHeader(HTTP_HEADER_CONTENT_TYPE)) {
                httpEntityEnclosingRequestBase.addHeader(HTTP_HEADER_CONTENT_TYPE, value + ";charset=utf-8");
            }

            if(text instanceof String) {
                if(null != multipartEntityBuilder) {
                    multipartEntityBuilder.addTextBody(metaType.name(), (String) text, metaTypeToContentType(metaType));
                    return;
                }
                httpEntityEnclosingRequestBase.setEntity(new StringEntity((String) text, "utf-8"));
            } else if(text instanceof byte[]) {
                if(MetaType.BYTES == metaType) {
                    if(null != multipartEntityBuilder) {
                        multipartEntityBuilder.addBinaryBody(metaType.name(), (byte[]) text);
                        return;
                    }
                    httpEntityEnclosingRequestBase.setEntity(new ByteArrayEntity((byte[]) text));
                } else if(MetaType.NBYTES == metaType) {
                    if(null != multipartEntityBuilder) {
                        multipartEntityBuilder.addBinaryBody(metaType.name(), (byte[]) text);
                        return;
                    }
                    httpEntityEnclosingRequestBase.setEntity(new NByteArrayEntity((byte[]) text));
                }
            }
        }
    }
}

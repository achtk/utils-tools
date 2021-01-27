package com.chua.utils.http.httpclient.handler;

import com.chua.utils.http.httpclient.action.HttpAsyncAction;
import com.chua.utils.tools.common.charset.CharsetHelper;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.entity.ResponseEntity;
import com.chua.utils.tools.http.meta.MetaType;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
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
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
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
import java.util.*;
import java.util.concurrent.Future;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

/**
 * HttpClient工具类
 *
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
     * 获取客户端
     *
     * @return
     */
    protected CloseableHttpClient getClient() {
        CloseableHttpClient client = (CloseableHttpClient) requestConfig.getClient();
        return null == client ? getCustomClient() : client;
    }

    /**
     * 获取客户端
     *
     * @return
     */
    protected CloseableHttpAsyncClient getAsyncClient() {
        CloseableHttpAsyncClient client = (CloseableHttpAsyncClient) requestConfig.getClient();
        return null == client ? getCustomAsyncClient() : client;
    }

    /**
     * 获取客户端
     *
     * @return
     */
    private CloseableHttpAsyncClient getCustomAsyncClient() {
        HttpAsyncClientBuilder httpAsyncClientBuilder = HttpAsyncClients.custom();

        //对照UA字串的标准格式理解一下每部分的意思
        httpAsyncClientBuilder.setUserAgent("Mozilla/5.0(Windows;U;Windows NT 5.1;en-US;rv:0.9.4)");

        if (requestConfig.getMaxConnTotal() > 0) {
            httpAsyncClientBuilder.setMaxConnTotal(requestConfig.getMaxConnTotal());
        }
        if (requestConfig.getMaxConnRoute() > 0) {
            httpAsyncClientBuilder.setMaxConnPerRoute(requestConfig.getMaxConnRoute());
        }


        org.apache.http.client.config.RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig.custom();
        if (requestConfig.getConnectTimeout() > 0) {
            //连接超时,连接建立时间,三次握手完成时间
            builder.setConnectTimeout(requestConfig.getConnectTimeout().intValue());
        }

        if (requestConfig.getTimeout() > 0) {
            //连接超时,连接建立时间,三次握手完成时间
            builder.setConnectionRequestTimeout(requestConfig.getTimeout().intValue());
        }

        if (requestConfig.getSocketTimeout() > 0) {
            //连接超时,连接建立时间,三次握手完成时间
            builder.setSocketTimeout(requestConfig.getSocketTimeout().intValue());
        }

        org.apache.http.client.config.RequestConfig config = builder.build();

        //配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
                setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setSoKeepAlive(true)
                .build();
        //设置连接池大小
        ConnectingIOReactor ioReactor = null;
        PoolingNHttpClientConnectionManager connManager = null;

        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
            connManager = new PoolingNHttpClientConnectionManager(ioReactor, registry());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (requestConfig.getMaxConnRoute() > 0) {
            //per route最大连接数设置
            connManager.setDefaultMaxPerRoute(requestConfig.getMaxConnRoute());
        }

        if (requestConfig.getMaxConnTotal() > 0) {
            //最大连接数设置1
            connManager.setMaxTotal(requestConfig.getMaxConnTotal());
        }


        httpAsyncClientBuilder.setConnectionManager(connManager);
        httpAsyncClientBuilder.setDefaultRequestConfig(config);

        CloseableHttpAsyncClient client = null;
        if (!requestConfig.isHttps()) {
            client = httpAsyncClientBuilder.build();
        } else {
            client = httpAsyncClientBuilder.setSSLContext(getSslContext()).build();
        }
        client.start();

        return client;
    }

    /**
     * 获取客户端
     *
     * @return
     */
    private CloseableHttpClient getCustomClient() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        //对照UA字串的标准格式理解一下每部分的意思
        httpClientBuilder.setUserAgent("Mozilla/5.0(Windows;U;Windows NT 5.1;en-US;rv:0.9.4)");

        if (requestConfig.getRetry() > 0) {
            httpClientBuilder.setRetryHandler(new StandardHttpRequestRetryHandler(requestConfig.getRetry(), false));
        }

        if (!Strings.isNullOrEmpty(requestConfig.getDns())) {
            httpClientBuilder.setDnsResolver(new DnsResolver() {
                @Override
                public InetAddress[] resolve(String host) throws UnknownHostException {
                    return InetAddress.getAllByName(requestConfig.getDns());
                }
            });
        }

        if (requestConfig.getMaxConnTotal() > 0) {
            httpClientBuilder.setMaxConnTotal(requestConfig.getMaxConnTotal());
        }
        if (requestConfig.getMaxConnRoute() > 0) {
            httpClientBuilder.setMaxConnPerRoute(requestConfig.getMaxConnRoute());
        }

        org.apache.http.client.config.RequestConfig.Builder builder = org.apache.http.client.config.RequestConfig.custom();
        if (requestConfig.getConnectTimeout() > 0) {
            builder.setConnectTimeout(requestConfig.getConnectTimeout().intValue());
        }

        if (requestConfig.getTimeout() > 0) {
            builder.setConnectionRequestTimeout(requestConfig.getTimeout().intValue());
        }

        if (requestConfig.getSocketTimeout() > 0) {
            builder.setSocketTimeout(requestConfig.getSocketTimeout().intValue());
        }

        org.apache.http.client.config.RequestConfig config = builder.build();
        HttpClientConnectionManager connManager = null;
        try {
            connManager = new PoolingHttpClientConnectionManager();
        } catch (Exception e) {
        }

        httpClientBuilder.setConnectionManager(connManager);
        httpClientBuilder.setDefaultRequestConfig(config);
        if (requestConfig.getMaxConnRoute() > 0) {
            httpClientBuilder.setMaxConnPerRoute(requestConfig.getMaxConnRoute());
        }

        if (requestConfig.getMaxConnTotal() > 0) {
            httpClientBuilder.setMaxConnTotal(requestConfig.getMaxConnTotal());
        }

        if (!requestConfig.isHttps()) {
            return httpClientBuilder.build();
        }

        try {
            return httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory()).build();
        } catch (Exception e) {
        }
        return httpClientBuilder.build();
    }


    /**
     * 获取sslContext
     *
     * @return
     */
    private SSLContext getSslContext() {
        if (requestConfig.isHttps()) {
            if (null == requestConfig.getSslSocketFactory()) {
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
        Future<HttpResponse> execute = httpClient.execute(httpMethod, new FutureCallback<HttpResponse>() {
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
        execute.get();
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
        if (log.isDebugEnabled()) {
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
            if (log.isDebugEnabled()) {
                log.debug("响应状态码: {}", httpResponse.getStatusLine().getStatusCode());
                log.debug("响应数据是否为空: {}", null != content && !content.isEmpty());
            }
            if (log.isTraceEnabled()) {
                log.trace("响应数据: {}", content);
            }
            return new ResponseEntity(httpResponse.getStatusLine().getStatusCode(), content);
        }
        if (log.isDebugEnabled()) {
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
            if (null == multipartEntityBuilder) {
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
     * @return
     * @throws UnsupportedEncodingException
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
     *
     * @param headers
     * @param httpMethod
     */
    public void packageHeader(Multimap<String, String> headers, HttpRequestBase httpMethod) {
        // 封装请求头
        if (headers == null) {
            return;
        }
        Set<String> keySet = headers.keySet();
        for (String key : keySet) {
            Collection<String> values = headers.get(key);
            for (String value : values) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(key, value);
            }
        }
    }

    /**
     * 设置实体
     *
     * @param httpEntityEnclosingRequestBase
     * @return
     */
    protected MultipartEntityBuilder resetEntityParams(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
        Map<String, Object> body = requestConfig.getBody();
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            multipartEntityBuilder.addBinaryBody(entry.getKey(), (InputStream) entry.getValue());
        }

        return multipartEntityBuilder;
    }


    /**
     * 设置json数据
     *
     * @param httpEntityEnclosingRequestBase
     */
    protected void resetJsonParams(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
        if (!httpEntityEnclosingRequestBase.containsHeader(CONTENT_TYPE)) {
            httpEntityEnclosingRequestBase.addHeader(CONTENT_TYPE, "application/json;charset=utf-8");
        } else {
            boolean hasJsonHeader = false;
            Header[] headers = httpEntityEnclosingRequestBase.getHeaders(CONTENT_TYPE);
            for (Header header : headers) {
                if ("application/json".equalsIgnoreCase(header.getValue())) {
                    hasJsonHeader = true;
                    break;
                }
            }
            if (hasJsonHeader) {
                httpEntityEnclosingRequestBase.addHeader(CONTENT_TYPE, "application/json;charset=utf-8");
            }
        }
    }

    /**
     * application/json请求
     *
     * @return
     */
    protected boolean isJson() {
        return requestConfig.hasHeaderJson();
    }

    /**
     * @return
     */
    protected boolean isStream() {
        Map<String, Object> body = requestConfig.getBody();
        for (Object value : body.values()) {
            if (value instanceof InputStream) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param metaType
     * @return
     */
    protected ContentType metaTypeToContentType(MetaType metaType) {
        return ContentType.create(metaType.getValue());
    }

    /**
     * 设置消息体
     *
     * @param bodyes
     * @param httpEntityEnclosingRequestBase
     */
    public void resetParams(Map<String, Object> bodyes, HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
        MultipartEntityBuilder multipartEntityBuilder = null;
        if (isStream()) {
            multipartEntityBuilder = resetEntityParams(httpEntityEnclosingRequestBase);
        }
        if (isJson()) {
            resetJsonParams(httpEntityEnclosingRequestBase);
            return;
        }

        //表单方式
        if (null != bodyes && !bodyes.isEmpty()) {
            // 封装请求参数
            try {
                packageParam(bodyes, multipartEntityBuilder, httpEntityEnclosingRequestBase);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Registry
     *
     * @return
     */
    @SuppressWarnings("ALL")
    public static Registry<SchemeIOSessionStrategy> registry() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, getTrustManager(), null);
        SSLIOSessionStrategy sslioSessionStrategy = new SSLIOSessionStrategy(sslContext, SSLIOSessionStrategy.ALLOW_ALL_HOSTNAME_VERIFIER);
        return RegistryBuilder.<SchemeIOSessionStrategy>create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", sslioSessionStrategy)
                .build();
    }

    /**
     * Registry
     *
     * @return
     */
    public static SSLConnectionSocketFactory sslConnectionSocketFactory() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, getTrustManager(), null);
        return new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    }

    /**
     * TrustManager[]
     *
     * @return
     */
    public static TrustManager[] getTrustManager() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }
                }
        };
    }
}

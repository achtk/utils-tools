package com.chua.utils.tools.http.sync;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.HttpClientHelper;
import com.chua.utils.tools.common.IoHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.constant.HttpConstant;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.entity.ResponseEntity;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.chua.utils.tools.constant.HttpConstant.*;

/**
 * 同步操作
 *
 * @author admin
 * @description
 * @updateTime 2020/5/30 22:53
 * @throws
 */
@Slf4j
@AllArgsConstructor
public class Sync {

    private final RequestConfig requestConfig;

    /**
     * get
     *
     * @throws
     * @description
     * @author admin
     * @updateTime 2020/5/30 22:54
     */
    public ResponseEntity executeGet() {
        return executeMethod(HttpConstant.HTTP_METHOD_GET);
    }

    /**
     * post 请求
     *
     * @throws
     * @author admin
     * @updateTime 2020/5/30 23:04
     */
    public ResponseEntity executePost() {
        return executeMethod(HttpConstant.HTTP_METHOD_POST);
    }

    /**
     * delete 请求
     *
     * @throws
     * @author admin
     * @updateTime 2020/5/30 23:04
     */
    public ResponseEntity executeDelete() {
        return executeMethod(HttpConstant.HTTP_METHOD_DELETE);
    }

    /**
     * put 请求
     *
     * @throws
     * @author admin
     * @updateTime 2020/5/30 23:04
     */
    public ResponseEntity executePut() {
        return executeMethod(HttpConstant.HTTP_METHOD_PUT);
    }

    /**
     * method请求
     *
     * @throws
     * @description
     * @author admin
     * @updateTime 2020/5/30 23:04
     */
    public ResponseEntity executeMethod(final String method) {
        try {
            HttpURLConnection connection = urlConnection();
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            connection.setDoInput(true);
            // 设定请求的方法，默认是GET
            connection.setRequestMethod(method);
            //设置消息头
            analysisHeader(connection);
            //设置缓存
            analysisCache(connection, method);
            //设置消息体
            alisander(connection, method);
            //设置配置
            analysisRequestConfig(connection);
            // 建立实际的连接
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）
            // 如果在已打开连接（此时 connected 字段的值为 true）的情况下调用 connect 方法，则忽略该调用
            if (log.isDebugEnabled()) {
                log.debug("==================================================");
            }
            log.info("发送的URL: {}", requestConfig.getUrl());
            if (log.isDebugEnabled()) {
                log.debug("消息体: {}", JsonHelper.toFormatJson(requestConfig.getBody()));
                log.debug("==================================================");
            }
            connection.connect();
            //获取结果
            int code = connection.getResponseCode();
            String content = null;
            byte[] bytes = null;
            try (InputStream stream = connection.getInputStream()) {
                bytes = IoHelper.toByteArray(stream);
                content = IoHelper.toString(bytes, "UTF-8");
            }
            log.info("接收到URL: {}, 响应码: {}", requestConfig.getUrl(), code);
            return new ResponseEntity(code, content).setBuffer(bytes);
        } catch (Throwable e) {
            e.printStackTrace();
            return new ResponseEntity(500, null, e.getMessage());
        }
    }

    /**
     * Post 请求不能使用缓存
     *
     * @param connection 链接
     * @param method     方法
     */
    private void analysisCache(HttpURLConnection connection, String method) {
        // Post 请求不能使用缓存
        if (Objects.equals(HttpConstant.HTTP_METHOD_GET, method)) {
            connection.setUseCaches(true);
            return;
        }
        connection.setUseCaches(false);
    }


    /**
     * 设置消息提
     *
     * @param connection 链接
     */
    private void alisander(HttpURLConnection connection, String method) {
        if (Objects.equals(HttpConstant.HTTP_METHOD_GET, method)) {
            String urlWithParameters = HttpClientHelper.createUrlWithParameters(requestConfig.getUrl(), requestConfig.getBody());
            requestConfig.setUrl(urlWithParameters);
            return;
        }
        if (!Objects.equals(HttpConstant.HTTP_METHOD_GET, method)) {
            Map<String, Object> body = requestConfig.getBody();
            if (BooleanHelper.hasLength(body)) {
                connection.setDoOutput(true);
                try (OutputStream outputStream = connection.getOutputStream();
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)) {
                    outputStreamWriter.write(HttpClientHelper.createWithParameters(body));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取链接
     *
     * @throws
     * @description
     * @author admin
     * @updateTime 2020/5/30 22:59
     */
    private HttpURLConnection urlConnection() throws IOException {
        URL realUrl = new URL(requestConfig.getUrl());
        if (!requestConfig.isHttps()) {
            // 打开和URL之间的连接
            return (HttpURLConnection) realUrl.openConnection();
        } else {
            HttpsURLConnection urlConnection = (HttpsURLConnection) realUrl.openConnection();
            Object sslSocketFactory = requestConfig.getSslSocketFactory();
            if (null != sslSocketFactory && sslSocketFactory instanceof SSLSocketFactory) {
                urlConnection.setSSLSocketFactory((SSLSocketFactory) sslSocketFactory);
            } else {
                HttpsURLConnection.setDefaultSSLSocketFactory(HttpClientHelper.createSslSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(HttpClientHelper.createDefaultHostnameVerifier());
            }
            return urlConnection;
        }
    }

    /**
     * 设置配置
     *
     * @throws
     * @description
     * @author admin
     * @updateTime 2020/5/30 22:59
     */
    private void analysisRequestConfig(URLConnection connection) {
        Long timeout = requestConfig.getConnectTimeout();
        // 设置一个指定的超时值（以毫秒为单位）
        if (null != timeout && timeout > 0L) {
            connection.setConnectTimeout(timeout.intValue());
        }

        Long readTimeout = requestConfig.getReadTimeout();
        // 将读超时设置为指定的超时，以毫秒为单位。
        if (null != readTimeout && readTimeout > 0L) {
            connection.setReadTimeout(readTimeout.intValue());
        }
        if (log.isDebugEnabled()) {
            log.debug("链接配置设置完成!!");
            log.debug("=======================================================");
            log.debug("connectTimeout: {}", timeout.intValue());
            log.debug("readTimeout: {}", readTimeout.intValue());
            log.debug("=======================================================");
        }
    }

    /**
     * 设置消息头
     *
     * @throws
     * @description
     * @author admin
     * @updateTime 2020/5/30 22:57
     */
    private void analysisHeader(URLConnection connection) {
        Multimap<String, String> headers = requestConfig.getHeaders();
        if (null == headers) {
            return;
        }

        Set<String> keySet = headers.keySet();
        for (String key : keySet) {
            Collection<String> strings = headers.get(key);
            for (String value : strings) {
                connection.setRequestProperty(key, value);
            }
        }

        // 设置通用的请求属性
        if (Strings.isNullOrEmpty(connection.getRequestProperty(ACCEPT))) {
            connection.setRequestProperty(ACCEPT, ANY);
        }

        // 设置通用的请求属性
        if (Strings.isNullOrEmpty(connection.getRequestProperty(CONNECTION))) {
            connection.setRequestProperty(CONNECTION, KEEP_ALIVE);
        }

        // 设置通用的请求属性
        if (Strings.isNullOrEmpty(connection.getRequestProperty(CONNECTION))) {
            connection.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
        }
        if (log.isDebugEnabled()) {
            if (log.isDebugEnabled()) {
                log.debug("消息头设置完成, header!!");
                log.debug("=======================================================");
                for (String key : keySet) {
                    log.debug("{}: {}", key, headers.get(key));
                }
                log.debug("=======================================================");
            }
        }
    }
}

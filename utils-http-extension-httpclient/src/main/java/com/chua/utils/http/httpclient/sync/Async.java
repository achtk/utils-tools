package com.chua.utils.http.httpclient.sync;

import com.chua.utils.http.httpclient.action.HttpAsyncAction;
import com.chua.utils.http.httpclient.handler.HttpClientHandler;
import com.chua.utils.tools.http.callback.ResponseCallback;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.entity.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;

/**
 * 异步
 *
 * @author CH
 */
@Slf4j
public class Async extends HttpClientHandler {

    public Async(RequestConfig requestConfig) {
        super(requestConfig);
    }

    /**
     * put
     *
     * @param callback
     * @return
     */
    public ResponseEntity executeDelete(ResponseCallback callback) {
        Map<String, Object> bodyers = requestConfig.getBody();
        if (bodyers == null) {
            bodyers = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        }
        bodyers.put("_method", "delete");
        requestConfig.setBody(bodyers);
        return executePost(callback);
    }

    /**
     * put
     *
     * @param callback
     * @return
     */
    public ResponseEntity executePut(final ResponseCallback callback) {
        CloseableHttpAsyncClient httpClient = getAsyncClient();
        //
        String url = requestConfig.getUrl();
        //
        HttpPut httpPut = new HttpPut(url);
        // 设置请求头
        packageHeader(requestConfig.getHeaders(), httpPut);

        Map<String, Object> bodes = requestConfig.getBody();
        //设置消息体
        resetParams(bodes, httpPut);

        try {
            try {
                getHttpAsyncClientResult(httpClient, httpPut, new HttpAsyncAction() {
                    @Override
                    public void completed(Object result) {
                        callback.onResponse(new ResponseEntity(200, result));
                    }

                    @Override
                    public void failed(Exception ex) {
                        callback.onFailure(ex);
                    }

                    @Override
                    public void cancelled() {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                log.error("请求异常, 地址: {}, 请求参数: {}", url, bodes);
            }
        } finally {
            release(null, httpClient);
        }
        return null;
    }

    /**
     * port
     *
     * @param callback
     * @return
     */
    public ResponseEntity executePost(final ResponseCallback callback) {
        // 创建httpClient对象
        CloseableHttpAsyncClient httpClient = getAsyncClient();
        //
        String url = requestConfig.getUrl();
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头
        packageHeader(requestConfig.getHeaders(), httpPost);

        Map<String, Object> bodes = requestConfig.getBody();
        //设置消息体
        resetParams(bodes, httpPost);

        try {
            // 执行请求并获得响应结果
            getHttpAsyncClientResult(httpClient, httpPost, new HttpAsyncAction() {

                @Override
                public void completed(Object result) {
                    callback.onResponse(new ResponseEntity(200, result));
                }

                @Override
                public void failed(Exception ex) {
                    callback.onFailure(ex);
                }

                @Override
                public void cancelled() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("请求异常, 地址: {}, 请求参数: {}", url, bodes);
        } finally {
            // 释放资源
            release(null, httpClient);
        }
        return null;
    }

    /**
     * get
     *
     * @param callback
     * @return
     */
    public ResponseEntity executeGet(final ResponseCallback callback) {
        // 创建httpClient对象
        CloseableHttpAsyncClient httpClient = getAsyncClient();
        // 创建访问的地址
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = packageUriBuilder(requestConfig.getUrl(), requestConfig.getBody());
        } catch (URISyntaxException e) {
            log.error("获取uri失败!!", e);
            return null;
        }
        if (null == uriBuilder) {
            return null;
        }
        HttpGet httpGet = null;
        // 创建http对象
        try {
            httpGet = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            log.error("创建Get请求失败!!", e);
            return null;
        }
        if (null == httpGet) {
            return null;
        }
        packageHeader(requestConfig.getHeaders(), httpGet);

        try {
            // 执行请求并获得响应结果
            getHttpAsyncClientResult(httpClient, httpGet, new HttpAsyncAction() {

                @Override
                public void completed(Object result) {
                    if(result instanceof BasicHttpResponse) {
                        BasicHttpResponse basicHttpResponse = (BasicHttpResponse) result;

                        try {
                            callback.onResponse(new ResponseEntity(basicHttpResponse.getStatusLine().getStatusCode(), EntityUtils.toString(basicHttpResponse.getEntity(), StandardCharsets.UTF_8)));
                        } catch (IOException e) {
                            callback.onFailure(e);
                        }
                        return;
                    }
                    callback.onResponse(new ResponseEntity(200, result));
                }

                @Override
                public void failed(Exception ex) {
                    callback.onFailure(ex);
                }

                @Override
                public void cancelled() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("请求异常, 地址: {}", uriBuilder.toString());
        } finally {
            // 释放资源
            release(null, httpClient);
        }
        return null;
    }
}

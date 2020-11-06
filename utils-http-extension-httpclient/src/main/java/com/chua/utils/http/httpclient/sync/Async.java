package com.chua.utils.http.httpclient.sync;

import com.chua.utils.http.callback.Callback;
import com.chua.utils.http.config.RequestConfig;
import com.chua.utils.http.entity.HttpClientResponse;
import com.chua.utils.http.httpclient.action.HttpAsyncAction;
import com.chua.utils.http.httpclient.handler.HttpClientHandler;
import com.chua.utils.tools.constant.NumberConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;

/**
 * 异步
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
    public HttpClientResponse executeDelete(Callback callback) {
        Map<String, Object> bodyers = requestConfig.getBodyers();
        if (bodyers == null) {
            bodyers = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        }
        bodyers.put("_method", "delete");
        requestConfig.setBodyers(bodyers);
        return executePost(callback);
    }

    /**
     * put
     *
     * @param callback
     * @return
     */
    public HttpClientResponse executePut(final Callback callback) {
        CloseableHttpAsyncClient httpClient = getAsyncClient();
        //
        String url = requestConfig.getUrl();
        //
        HttpPut httpPut = new HttpPut(url);
        //
        httpPut.setConfig(setRequestConfig());

        // 设置请求头
        packageHeader(requestConfig.getHeaders(), httpPut);

        Map<String, Object> bodes = requestConfig.getBodyers();
        //设置消息体
        resetParams(bodes, httpPut);

        try {
            try {
                getHttpAsyncClientResult(httpClient, httpPut, new HttpAsyncAction() {
                    @Override
                    public void completed(Object result) {
                        callback.onResponse(new HttpClientResponse(200, result));
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
                log.error("请求异常, 地址: {}, 请求参数: {}", url, null != bodes && !bodes.isEmpty() ? bodes : requestConfig.getText());
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
    public HttpClientResponse executePost(final Callback callback) {
        // 创建httpClient对象
        CloseableHttpAsyncClient httpClient = getAsyncClient();
        //
        String url = requestConfig.getUrl();
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);

        httpPost.setConfig(setRequestConfig());
        // 设置请求头
        packageHeader(requestConfig.getHeaders(), httpPost);

        Map<String, Object> bodes = requestConfig.getBodyers();
        //设置消息体
        resetParams(bodes, httpPost);

        try {
            // 执行请求并获得响应结果
            getHttpAsyncClientResult(httpClient, httpPost, new HttpAsyncAction() {

                @Override
                public void completed(Object result) {
                    callback.onResponse(new HttpClientResponse(200, result));
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
            log.error("请求异常, 地址: {}, 请求参数: {}", url, null != bodes && !bodes.isEmpty() ? bodes : requestConfig.getText());
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
    public HttpClientResponse executeGet(final Callback callback) {
        // 创建httpClient对象
        CloseableHttpAsyncClient httpClient = getAsyncClient();
        // 创建访问的地址
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = packageUriBuilder(requestConfig.getUrl(), requestConfig.getBodyers());
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

        httpGet.setConfig(setRequestConfig());

        packageHeader(requestConfig.getHeaders(), httpGet);

        try {
            // 执行请求并获得响应结果
            getHttpAsyncClientResult(httpClient, httpGet, new HttpAsyncAction() {

                @Override
                public void completed(Object result) {
                    callback.onResponse(new HttpClientResponse(200, result));
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

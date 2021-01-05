package com.chua.utils.http.httpclient.sync;

import com.chua.utils.http.httpclient.handler.HttpClientHandler;
import com.chua.utils.tools.http.config.RequestConfig;
import com.chua.utils.tools.http.entity.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;

/**
 * 同步http
 *
 * @author Administrator
 */
@Slf4j
public class Sync extends HttpClientHandler {


    public Sync(RequestConfig requestConfig) {
        super(requestConfig);
    }

    /**
     * put
     *
     * @return
     */
    public ResponseEntity executeDelete() {
        Map<String, Object> bodes = requestConfig.getBody();
        if (bodes == null) {
            bodes = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        }
        bodes.put("_method", "delete");
        requestConfig.setBody(bodes);
        return executePost();
    }

    /**
     * put
     *
     * @return
     */
    public ResponseEntity executePut() {
        CloseableHttpClient httpClient = getClient();
        //
        String url = requestConfig.getUrl();
        //
        HttpPut httpPut = new HttpPut(url);
        //
        httpPut.setConfig(setRequestConfig());

        // 设置请求头
        packageHeader(requestConfig.getHeaders(), httpPut);

        Map<String, Object> bodes = requestConfig.getBody();
        resetParams(bodes, httpPut);

        CloseableHttpResponse httpResponse = null;

        try {
            return getHttpClientResult(httpResponse, httpClient, httpPut);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("请求异常, 地址: {}, 请求参数: {}", url, bodes);
        } finally {
            release(httpResponse, httpClient);
        }
        return null;
    }


    /**
     * port
     *
     * @return
     */
    public ResponseEntity executePost() {
        // 创建httpClient对象
        CloseableHttpClient httpClient = getClient();

        String url = requestConfig.getUrl();
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        //设置链接配置
        httpPost.setConfig(setRequestConfig());
        // 设置请求头
        packageHeader(requestConfig.getHeaders(), httpPost);

        Map<String, Object> bodyers = requestConfig.getBody();
        // 设置请求体
        resetParams(bodyers, httpPost);

        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;

        try {
            // 执行请求并获得响应结果
            return getHttpClientResult(httpResponse, httpClient, httpPost);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("请求异常, 地址: {}, 请求参数: {}", url, bodyers);
        } finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
        return null;
    }

    /**
     * get
     *
     * @return
     */
    public ResponseEntity executeGet() {
        // 创建httpClient对象
        CloseableHttpClient httpClient = getClient();
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
        httpGet.setConfig(setRequestConfig());
        packageHeader(requestConfig.getHeaders(), httpGet);

        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;

        try {
            // 执行请求并获得响应结果
            return getHttpClientResult(httpResponse, httpClient, httpGet);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("请求异常, 地址: {}", uriBuilder.toString());
        } finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
        return null;
    }

}

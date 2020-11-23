package com.chua.utils.tools.manager.operation;

import com.chua.utils.tools.http.entity.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * http操作接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public interface HttpOperations {

    // GET

    /**
     * 通过在指定的URL上进行GET检索表示形式。
     * 响应（如果有）被转换并返回。
     * <p> URI模板变量使用给定的URI变量（如果有）进行扩展。
     *
     * @param url          the url
     * @param responseType the type of the return value
     * @param uriVariables the variables to expand the template
     * @return the converted object
     */
    <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws IOException;

    /**
     * 通过在指定的URL上进行GET检索表示形式。
     * 响应（如果有）被转换并返回。
     * <p> URI模板变量使用给定的URI变量（如果有）进行扩展。
     *
     * @param url          the url
     * @param responseType the type of the return value
     * @param uriVariables the variables to expand the template
     * @return the converted object
     */
    <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws IOException;

    /**
     * 通过在指定的URL上进行POST检索表示形式。
     * 响应（如果有）被转换并返回。
     * <p> URI模板变量使用给定的URI变量（如果有）进行扩展。
     *
     * @param url          the url
     * @param responseType the type of the return value
     * @param uriVariables the variables to expand the template
     * @return the converted object
     */
    <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) throws IOException;

    /**
     * 执行
     *
     * @param url
     * @param method
     * @param requestCallback
     * @param responseExtractor
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> ResponseEntity<T> execute(URI url, String method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws IOException;

    /**
     * 请求处理器
     */
    interface RequestCallback {
        /**
         * 获取请求头
         *
         * @return 请求头
         */
        Map<String, String> getHeaders();

        /**
         * 获取请求体
         *
         * @return 请求体
         */
        Object getBodyers();

    }

    /**
     * 响应提取器
     */
    interface ResponseExtractor<T> {
        /**
         * 转化
         *
         * @param responseEntity 结果
         * @return T
         */
        ResponseEntity<T> convert(ResponseEntity responseEntity);
    }
}

package com.chua.utils.tools.http.stream;


import com.chua.utils.tools.util.JsonUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.net.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpClient流式操作
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:11
 */
public class HttpClientStream {
    /**
     * 消息头
     */
    private Multimap<String, String> headers = HashMultimap.create();
    /**
     * 表单
     */
    private Map<String, Object> form = new HashMap<>();
    /**
     * json
     */
    private Map<String, Object> json = new HashMap<>();

    private static final String JSON_HEADER = "application/json";


    /**
     * 添加消息头
     *
     * @param headerName  消息头
     * @param headerValue 消息内容
     * @return this
     */
    public HttpClientStream header(final String headerName, final String headerValue) {
        headers.put(headerName, headerValue);
        return this;
    }

    /**
     * 添加消息头
     *
     * @param header 消息头
     * @return this
     */
    public HttpClientStream headers(final Map<String, String> header) {
        header.entrySet().forEach(entry -> {
            header(entry.getKey(), entry.getValue());
        });
        return this;
    }

    /**
     * 添加表单信息
     *
     * @param formName  消息头
     * @param formValue 消息内容
     * @return this
     */
    public HttpClientStream formBody(final String formName, final Object formValue) {
        form.put(formName, formValue);
        return this;
    }

    /**
     * 添加表单信息
     *
     * @param formValues 表单信息
     * @return this
     */
    public HttpClientStream formBody(final Map<String, Object> formValues) {
        formValues.entrySet().forEach(entry -> {
            formBody(entry.getKey(), entry.getValue());
        });
        return this;
    }

    /**
     * 添加json信息
     *
     * @param jsonStr json信息
     * @return this
     */
    public HttpClientStream json(final String jsonStr) {
        this.header(HttpHeaders.CONTENT_TYPE, JSON_HEADER);
        Map<String, Object> stringObjectMap = JsonUtils.fromJson2Map(jsonStr);
        json.putAll(stringObjectMap);
        return this;
    }

    /**
     * 添加json信息
     *
     * @param jsonName  json信息头
     * @param jsonValue json信息内容
     * @return this
     */
    public HttpClientStream json(final String jsonName, final Object jsonValue) {
        this.header(HttpHeaders.CONTENT_TYPE, JSON_HEADER);
        json.put(jsonName, jsonValue);
        return this;
    }
}

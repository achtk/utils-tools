package com.chua.utils.http.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 封装httpClient响应结果
 * @author CH
 * @date Created on 2018年4月19日
 */
@Getter
@Setter
public class HttpClientResponse implements Serializable {

    private static final long serialVersionUID = 2168152194164783950L;

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private Object content;

    private byte[] bytes;


    private String error;

    public HttpClientResponse() {
    }

    public HttpClientResponse(int code) {
        this.code = code;
    }

    public HttpClientResponse(Object content) {
        this.content = content;
    }

    public HttpClientResponse(int code, Object content) {
        this.code = code;
        this.content = content;
    }

    public HttpClientResponse(int code, Object content, String error) {
        this.code = code;
        this.content = content;
        this.error = error;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public HttpClientResponse setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }
}
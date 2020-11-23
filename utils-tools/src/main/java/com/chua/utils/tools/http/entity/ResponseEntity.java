package com.chua.utils.tools.http.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * 封装httpClient响应结果
 *
 * @author CH
 * @date Created on 2018年4月19日
 */
@Getter
@Setter
public class ResponseEntity<T> implements Serializable {

    private static final long serialVersionUID = 2168152194164783950L;

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private Object content;

    private ByteBuffer buffer;


    private String error;

    public ResponseEntity() {
    }

    public ResponseEntity(int code) {
        this.code = code;
    }

    public ResponseEntity(Object content) {
        this.content = content;
    }

    public ResponseEntity(int code, Object content) {
        this.code = code;
        this.content = content;
    }

    public ResponseEntity(int code, Object content, String error) {
        this.code = code;
        this.content = content;
        this.error = error;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public ResponseEntity setBuffer(byte[] buffer) {
        this.buffer = null == buffer ? null : ByteBuffer.wrap(buffer);
        return this;
    }
}
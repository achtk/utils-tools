package com.chua.utils.http.http;

import com.chua.utils.http.stream.AbstractHttpClientStream;

/**
 * HttpClient统一接口
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:08
 */
public interface IHttpClientFactory {
    /**
     * get
     * @return
     */
    public AbstractHttpClientStream newGet();

    /**
     * post
     * @return
     */
    public AbstractHttpClientStream newPost();

    /**
     * put
     * @return
     */
    public AbstractHttpClientStream newPut();

    /**
     * delete
     * @return
     */
    public AbstractHttpClientStream newDelete();

    /**
     * method
     * @param type 方法
     * @return
     */
    default public AbstractHttpClientStream newMethod(String type) {
        if(null == type) {
            return newGet();
        }

        type = type.toUpperCase();
        if("GET".equals(type)) {
            return newGet();
        }
        if("POST".equals(type)) {
            return newPost();
        }
        if("PUT".equals(type)) {
            return newPut();
        }
        if("DELETE".equals(type)) {
            return newDelete();
        }
        return newGet();
    }
}

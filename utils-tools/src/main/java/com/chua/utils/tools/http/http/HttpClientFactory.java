package com.chua.utils.tools.http.http;

import com.chua.utils.tools.http.stream.HttpClientStream;
import com.chua.utils.tools.spi.Spi;

import static com.chua.utils.tools.constant.HttpConstant.*;

/**
 * HttpClient统一接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:08
 */
@Spi("url")
public interface HttpClientFactory {
    /**
     * get
     *
     * @return
     */
    HttpClientStream newGet();

    /**
     * post
     *
     * @return
     */
    HttpClientStream newPost();

    /**
     * put
     *
     * @return
     */
    HttpClientStream newPut();

    /**
     * delete
     *
     * @return
     */
    HttpClientStream newDelete();

    /**
     * method
     *
     * @param type 方法
     * @return
     */
    default HttpClientStream newMethod(String type) {
        if (null == type) {
            return newGet();
        }

        type = type.toUpperCase();
        if (HTTP_METHOD_GET.equals(type)) {
            return newGet();
        }
        if (HTTP_METHOD_POST.equals(type)) {
            return newPost();
        }
        if (HTTP_METHOD_PUT.equals(type)) {
            return newPut();
        }
        if (HTTP_METHOD_DELETE.equals(type)) {
            return newDelete();
        }
        return newGet();
    }
}

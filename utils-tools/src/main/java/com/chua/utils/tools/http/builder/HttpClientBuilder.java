package com.chua.utils.tools.http.builder;


import com.chua.utils.tools.http.callback.ResponseCallback;
import com.chua.utils.tools.http.entity.ResponseEntity;

/**
 * Client构造
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/3/21 14:20
 */
public interface HttpClientBuilder {

    /**
     * 执行
     *
     * @return
     */
    ResponseEntity execute();

    /**
     * 执行
     *
     * @param responseCallback 回调
     */
    void execute(ResponseCallback responseCallback);
}

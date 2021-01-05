package com.chua.utils.tools.http.stream;

import com.chua.utils.tools.http.builder.HttpClientBuilder;

/**
 * 客户端流
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/5
 */
public interface ClientStream {


    /**
     * 构建流
     *
     * @return 流
     */
    HttpClientBuilder build();
}

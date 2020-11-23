package com.chua.utils.tools.http;

import lombok.Data;

import java.util.Map;

/**
 * 请求实体
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
@Data
public class HttpEntity {

    private final Map<String, String> headers;
    private final Map<String, Object> body;
}

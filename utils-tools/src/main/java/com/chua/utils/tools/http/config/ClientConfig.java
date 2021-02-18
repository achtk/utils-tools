package com.chua.utils.tools.http.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端自定义
 *
 * @author CH
 */
@Getter
@Setter
public class ClientConfig {
    /**
     * 最大连接数
     */
    private int maxTotal = 20;
    /**
     * 每个路由最大连接数
     */
    private int maxPerRoute = 5;
    /**
     * 重试次数
     */
    private int retry = 3;
}

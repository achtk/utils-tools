package com.chua.utils.http.okhttp.config;

import lombok.Getter;
import lombok.Setter;

import java.net.SocketAddress;

/**
 * @author CH
 */
@Getter@Setter
public class ProxyConfig {
    /**
     * 类型
     */
    private String type;
    /**
     * 地址
     */
    private SocketAddress socketAddress;
}

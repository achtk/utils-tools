package com.chua.utils.tools.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 基础服务配置项
 *
 * @author CH
 */
@Getter
@Setter
@Accessors(chain = true)
public class ServerConfig<T> {
    /**
     * 重复次数
     */
    private int retry = 3;
    /**
     * 连接超时
     */
    private int connectTimeoutMillis = 1000;
    /**
     * 读取超时
     */
    private int readTimeoutMillis = 3500;

    public int getRetry(int defaultRetry) {
        return retry > 0 ? retry : defaultRetry;
    }

    public int getConnectTimeoutMillis(int defaultConnectTimeoutMillis) {
        return connectTimeoutMillis > 0 ? connectTimeoutMillis : defaultConnectTimeoutMillis;
    }

    public int getReadTimeoutMillis(int defaultReadTimeoutMillis) {
        return readTimeoutMillis > 0 ? readTimeoutMillis : defaultReadTimeoutMillis;
    }
}

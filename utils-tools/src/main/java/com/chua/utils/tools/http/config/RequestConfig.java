package com.chua.utils.tools.http.config;

import com.chua.utils.tools.http.exception.ThrowableHandler;
import com.chua.utils.tools.http.meta.MetaType;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author CHTK
 */
@Getter
@Setter
public class RequestConfig<Client> {

    /**
     * 最大连接数
     */
    private int maxConnTotal = 24;
    /**
     * 最大连接路由
     */
    private int maxConnRoute = 3;
    /**
     * 重试
     */
    private int retry = 3;
    /**
     *
     */
    private String method;
    /**
     *
     */
    private String url;
    /**
     * 连接超时
     */
    private Long connectTimeout = 60000L;
    /**
     * 读取尝试
     */
    private Long readTimeout = 50000L;
    /**
     * 会话超时
     */
    private Long socketTimeout = 5000L;
    /**
     * 是否开启权限
     */
    private boolean authenticationEnabled;
    /**
     * ssl
     */
    private Object sslSocketFactory;
    /**
     * 是否是https
     */
    private boolean https;
    /**
     * 是否压缩
     */
    private boolean contentCompressionEnabled;
    /**
     * 客户端
     */
    private Client client;
    /**
     *
     */
    private String dns;
    /**
     *
     */
    private List<String> protocols;

    private Object text;
    private MetaType metaType;

    public void setText(String text, MetaType metaType) {
        this.metaType = metaType;
        this.text = text;
    }

    private Map<String, String> headers;
    private Map<String, Object> bodyers;
    private Map<String, InputStream> streams;

    private ThrowableHandler handler;
}

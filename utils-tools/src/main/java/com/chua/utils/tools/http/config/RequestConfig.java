package com.chua.utils.tools.http.config;

import com.chua.utils.tools.http.exception.ThrowableHandler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author CHTK
 */
@Getter
@Setter
public class RequestConfig<Client> {
    /**
     * 请求地址
     */
    private String url;
    /**
     * 重试
     */
    private int retry = 3;
    /**
     * 方法
     */
    private String method;
    /**
     * 消息头
     */
    private Multimap<String, String> headers = HashMultimap.create();
    /**
     * 表单
     */
    private Map<String, Object> body = new HashMap<>();
    /**
     * 表单
     */
    private Map<String, String> cookie = new HashMap<>();
    /**
     * JSON
     */
    public static final String JSON_HEADER = "application/json";
    /**
     * 超时时间
     */
    private Long timeout = 60_000L;
    /**
     * 异常回调
     */
    private ThrowableHandler handler;
    /**
     * 是否是https
     */
    public boolean https;

    /**
     * sslSocketFactory
     *
     * @return
     */
    public Object sslSocketFactory;

    /**
     * 最大连接数
     */
    private int maxConnTotal = 24;
    /**
     * 最大连接路由
     */
    private int maxConnRoute = 3;

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
     * 协议
     */
    public List<String> protocols;
    /**
     * dns
     */
    public String dns;

    /**
     * 是否开启权限
     */
    private boolean authenticationEnabled;

    /**
     * 是否压缩
     */
    private boolean contentCompressionEnabled;
    /**
     * 客户端
     */
    private Client client;


    /**
     * 异常回调
     */
    private Consumer<Throwable> throwableConsumer;

    /**
     * 消息头是否存在json
     *
     * @return 存在json返回true
     */
    public boolean hasHeaderJson() {
        return this.headers.containsValue(JSON_HEADER);
    }

}

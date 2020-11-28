package com.chua.tools.spider.request;

import com.chua.tools.spider.proxy.PageProxy;
import lombok.Data;

import java.net.Proxy;
import java.util.Map;

/**
 * 页面请求
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Data
public class PageRequest {
    private String url;
    private Map<String, String> paramMap;
    private Map<String, String> cookieMap;
    private Map<String, String> headerMap;
    private String userAgent;
    private String referrer;
    private boolean ifPost;
    private int timeoutMillis;
    private boolean isValidateTlsCertificates;
    private PageProxy proxy;
}

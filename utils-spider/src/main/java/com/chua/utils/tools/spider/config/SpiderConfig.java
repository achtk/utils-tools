package com.chua.utils.tools.spider.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * 基础信息配置
 * @author CH
 */
@Data
@EqualsAndHashCode
public class SpiderConfig {

    private String domain;

    private String userAgent;

    private Map<String, String> defaultCookies = new LinkedHashMap<String, String>();

    private Map<String, Map<String, String>> cookies = new HashMap<String, Map<String, String>>();

    private String charset;

    private int sleepTime = 5000;

    private int retryTimes = 0;

    private int cycleRetryTimes = 0;

    private int retrySleepTime = 1000;

    private int timeOut = 5000;

    private static final Set<Integer> DEFAULT_STATUS_CODE_SET = new HashSet<Integer>();

    private Set<Integer> acceptStatCode = DEFAULT_STATUS_CODE_SET;

    private Map<String, String> headers = new HashMap<String, String>();

    private boolean useGzip = true;

    private boolean disableCookieManagement = false;
}

package com.chua.tools.spider.config;

import com.chua.tools.spider.constant.UserAgentConstant;
import com.chua.tools.spider.loader.JsoupPageLoader;
import com.chua.tools.spider.loader.PageLoader;
import com.chua.tools.spider.parser.Parser;
import com.chua.tools.spider.process.ApiParserProcessor;
import com.chua.tools.spider.process.PageParserProcessor;
import com.chua.tools.spider.process.ParserProcessor;
import com.chua.tools.spider.proxy.PageProxy;
import com.chua.tools.spider.util.RegexUtil;
import com.chua.tools.spider.util.UrlUtil;
import lombok.Data;

import java.util.*;

/**
 * 配置项
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Data
public class CrawlerConf {
    /**
     * 页面加载器
     */
    private PageLoader pageLoader = new JsoupPageLoader();
    /**
     * 进程解析器
     */
    private List<ParserProcessor> processors = new ArrayList<ParserProcessor>() {
        {
            add(new PageParserProcessor());
            add(new ApiParserProcessor());
        }
    };
    /**
     * 页面代理
     */
    private PageProxy pageProxy;
    /**
     * 页面处理器
     */
    private List<Parser> parser = new ArrayList<>();

    /**
     * 失败重试次数，大于零时生效
     */
    private volatile int failRetryCount = 0;
    /**
     * 请求方式：true=POST请求、false=GET请求
     */
    private volatile boolean post = false;

    /**
     * URL白名单正则，非空时进行URL白名单过滤页面
     */
    private Set<String> whiteUrlRegex = Collections.synchronizedSet(Collections.emptySet());
    /**
     * 是否验证https
     */
    private volatile boolean isValidateTlsCertificates = true;

    /**
     * 是否扩散
     */
    private volatile boolean allowSpread = true;
    /**
     * 请求参数
     */
    private volatile Map<String, String> paramMap;
    /**
     * 请求Cookie
     */
    private volatile Map<String, String> cookieMap;
    /**
     * 请求头
     */
    private volatile Map<String, String> headerMap;

    /**
     * 请求UserAgent
     */
    private volatile List<String> userAgentList = Collections.synchronizedList(Collections.singletonList(UserAgentConstant.USER_AGENT_CHROME));
    /**
     * 请求Referrer
     */
    private volatile String referrer;
    /**
     * 超时时间
     */
    private volatile int timeoutMillis = 5 * 1000;

    /**
     * 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
     */
    private volatile int pauseMillis = 0;


    /**
     * valid url, include white url
     *
     * @param link
     * @return boolean
     */
    public boolean validWhiteUrl(String link) {
        if (!UrlUtil.isUrl(link)) {
            // false if url invalid
            return false;
        }

        if (whiteUrlRegex != null && whiteUrlRegex.size() > 0) {
            boolean underWhiteUrl = false;
            for (String whiteRegex : this.whiteUrlRegex) {
                if (RegexUtil.matches(whiteRegex, link)) {
                    underWhiteUrl = true;
                }
            }
            if (!underWhiteUrl) {
                return false;
            }
        }
        return true;
    }
}

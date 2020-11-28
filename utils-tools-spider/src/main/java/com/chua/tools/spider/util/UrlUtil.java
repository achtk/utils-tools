package com.chua.tools.spider.util;

import com.chua.tools.spider.config.CrawlerConf;
import com.chua.tools.spider.crawler.Crawler;
import com.chua.tools.spider.request.PageRequest;

import java.util.Random;

/**
 * url工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class UrlUtil {

    /**
     * url格式校验
     *
     * @param url url
     * @return 是url返回true
     */
    public static boolean isUrl(String url) {
        return url != null && url.trim().length() > 0 && url.startsWith("http");
    }

    /**
     * 获取pageRequest
     *
     * @param crawler 爬虫配置
     * @param url     地址
     * @return PageRequest
     */
    public static PageRequest createPageRequest(Crawler crawler, String url) {
        CrawlerConf crawlerConf = crawler.getCrawlerConf();

        String userAgent = crawlerConf.getUserAgentList().size() > 1
                ? crawlerConf.getUserAgentList().get(new Random().nextInt(crawlerConf.getUserAgentList().size()))
                : crawlerConf.getUserAgentList().size() == 1 ? crawlerConf.getUserAgentList().get(0) : null;

        PageRequest pageRequest = new PageRequest();
        pageRequest.setUrl(url);
        pageRequest.setParamMap(crawlerConf.getParamMap());
        pageRequest.setCookieMap(crawlerConf.getCookieMap());
        pageRequest.setHeaderMap(crawlerConf.getHeaderMap());
        pageRequest.setUserAgent(userAgent);
        pageRequest.setReferrer(crawlerConf.getReferrer());
        pageRequest.setIfPost(crawlerConf.isPost());
        pageRequest.setTimeoutMillis(crawlerConf.getTimeoutMillis());
        pageRequest.setProxy(null);
        pageRequest.setValidateTlsCertificates(crawlerConf.isValidateTlsCertificates());

        return pageRequest;
    }
}

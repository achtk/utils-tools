package com.chua.tools.spider.crawler;

import com.chua.tools.spider.config.CrawlerConf;
import com.chua.tools.spider.loader.PageLoader;
import com.chua.tools.spider.parser.PageParser;
import com.chua.tools.spider.proxy.PageProxy;
import com.chua.tools.spider.task.CrawlerTask;
import com.chua.tools.spider.task.Task;
import com.chua.tools.spider.url.LocaleUrlLoader;
import com.chua.tools.spider.url.UrlLoader;
import com.chua.utils.tools.common.ThreadHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 爬虫
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Slf4j
@Data
public class Crawler {
    /**
     * url获取器
     */
    private volatile UrlLoader urlLoader = new LocaleUrlLoader();
    /**
     * 基础配置
     */
    private volatile CrawlerConf crawlerConf = new CrawlerConf();
    /**
     * 爬虫线程数量
     */
    private int threadCount = 1;
    /**
     * 最大线程数
     */
    private static final int THREAD_MAX_COUNT = 1000;
    /**
     * 检验时间
     */
    private static final int TEST_OF_TIME = 5;
    /**
     * 爬虫线程池
     */
    private ExecutorService crawlerService = ThreadHelper.newCachedThreadPool();
    /**
     * 爬虫线程引用镜像
     */
    private List<Task> crawlerTasks = new CopyOnWriteArrayList<>();

    /**
     * ---------------------- builder ----------------------
     */
    public static class Builder {

        private Crawler crawler = new Crawler();

        /**
         * 设置运行数据类型
         *
         * @param urlLoader 数据类型
         * @return Builder
         */
        public Builder setUrlLoader(UrlLoader urlLoader) {
            crawler.urlLoader = urlLoader;
            return this;
        }

        /**
         * 待爬的URL列表
         *
         * @param urls URL列表
         * @return Builder
         */
        public Builder setUrls(String... urls) {
            if (urls != null && urls.length > 0) {
                for (String url : urls) {
                    crawler.urlLoader.addUrl(url);
                }
            }
            return this;
        }

        /**
         * 允许扩散爬取，将会以现有URL为起点扩散爬取整站
         *
         * @param allowSpread allowSpread
         * @return Builder
         */
        public Builder setAllowSpread(boolean allowSpread) {
            crawler.crawlerConf.setAllowSpread(allowSpread);
            return this;
        }

        /**
         * URL白名单正则，非空时进行URL白名单过滤页面
         *
         * @param whiteUrlRegex whiteUrlRegex
         * @return Builder
         */
        public Builder setWhiteUrlRegex(String... whiteUrlRegex) {
            if (whiteUrlRegex != null && whiteUrlRegex.length > 0) {
                for (String item : whiteUrlRegex) {
                    crawler.crawlerConf.getWhiteUrlRegex().add(item);
                }
            }
            return this;
        }

        /**
         * 页面解析器
         *
         * @param pageParser pageParser
         * @return Builder
         */
        public Builder setPageParser(PageParser pageParser) {
            crawler.crawlerConf.getParser().add(pageParser);
            return this;
        }

        /**
         * 页面下载器
         *
         * @param pageLoader pageLoader
         * @return Builder
         */
        public Builder setPageLoader(PageLoader pageLoader) {
            crawler.crawlerConf.setPageLoader(pageLoader);
            return this;
        }

        // site

        /**
         * 请求参数
         *
         * @param paramMap paramMap
         * @return Builder
         */
        public Builder setParamMap(Map<String, String> paramMap) {
            crawler.crawlerConf.setParamMap(paramMap);
            return this;
        }

        /**
         * 请求Cookie
         *
         * @param cookieMap cookieMap
         * @return Builder
         */
        public Builder setCookieMap(Map<String, String> cookieMap) {
            crawler.crawlerConf.setCookieMap(cookieMap);
            return this;
        }

        /**
         * 请求Header
         *
         * @param headerMap headerMap
         * @return Builder
         */
        public Builder setHeaderMap(Map<String, String> headerMap) {
            crawler.crawlerConf.setHeaderMap(headerMap);
            return this;
        }

        /**
         * 请求UserAgent
         *
         * @param userAgents userAgents
         * @return Builder
         */
        public Builder setUserAgent(String... userAgents) {
            if (userAgents != null && userAgents.length > 0) {
                for (String userAgent : userAgents) {
                    if (!crawler.crawlerConf.getUserAgentList().contains(userAgent)) {
                        crawler.crawlerConf.getUserAgentList().add(userAgent);
                    }
                }
            }
            return this;
        }

        /**
         * 请求Referrer
         *
         * @param referrer Referrer
         * @return Builder
         */
        public Builder setReferrer(String referrer) {
            crawler.crawlerConf.setReferrer(referrer);
            return this;
        }

        /**
         * 请求方式：true=POST请求、false=GET请求
         *
         * @param isPost 请求方式
         * @return Builder
         */
        public Builder setIfPost(boolean isPost) {
            crawler.crawlerConf.setPost(isPost);
            return this;
        }

        /**
         * 超时时间，毫秒
         *
         * @param timeoutMillis 超时时间
         * @return Builder
         */
        public Builder setTimeoutMillis(int timeoutMillis) {
            crawler.crawlerConf.setTimeoutMillis(timeoutMillis);
            return this;
        }

        /**
         * 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
         *
         * @param pauseMillis 停顿时间
         * @return Builder
         */
        public Builder setPauseMillis(int pauseMillis) {
            crawler.crawlerConf.setPauseMillis(pauseMillis);
            return this;
        }

        /**
         * 页面代理
         *
         * @param pageProxy 页面代理
         * @return Builder
         */
        public Builder setProxyMaker(PageProxy pageProxy) {
            crawler.crawlerConf.setPageProxy(pageProxy);
            return this;
        }

        /**
         * 失败重试次数，大于零时生效
         *
         * @param failRetryCount 失败重试次数
         * @return Builder
         */
        public Builder setFailRetryCount(int failRetryCount) {
            if (failRetryCount > 0) {
                crawler.crawlerConf.setFailRetryCount(failRetryCount);
            }
            return this;
        }

        /**
         * 爬虫并发线程数
         *
         * @param threadCount 爬虫并发线程数
         * @return Builder
         */
        public Builder setThreadCount(int threadCount) {
            crawler.threadCount = threadCount;
            return this;
        }

        public Crawler build() {
            return crawler;
        }
    }


    /**
     * ---------------------- crawler thread ----------------------
     * 启动
     */
    private void start(boolean sync) {
        if (urlLoader == null) {
            throw new RuntimeException("crawler urlLoader can not be null.");
        }

        if (urlLoader.nowNum() <= 0) {
            throw new RuntimeException("crawler indexUrl can not be empty.");
        }

        if (threadCount < 1 || threadCount > THREAD_MAX_COUNT) {
            throw new RuntimeException("crawler threadCount invalid, threadCount : " + threadCount);
        }

        if (crawlerConf.getPageLoader() == null) {
            throw new RuntimeException("crawler pageLoader can not be null.");
        }

        if (crawlerConf.getParser() == null) {
            throw new RuntimeException("crawler pageParser can not be null.");
        }

        log.info(">>>>>>>>>>> start ...");
        for (int i = 0; i < threadCount; i++) {
            Task crawlerThread = new CrawlerTask(this);
            crawlerTasks.add(crawlerThread);
        }
        for (Task task : crawlerTasks) {
            crawlerService.execute(task);
        }
        crawlerService.shutdown();

        if (sync) {
            try {
                while (!crawlerService.awaitTermination(TEST_OF_TIME, TimeUnit.SECONDS)) {
                    log.info(">>>>>>>>>>> crawler still running ...");
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 同步运行
     */
    public void run() {
        start(true);
    }

    /**
     * 异步运行
     */
    public void runAsync() {
        start(false);
    }

    /**
     * 终止
     */
    public void stop() {
        for (Task task : crawlerTasks) {
            task.toStop();
        }
        crawlerService.shutdownNow();
        log.info(">>>>>>>>>>> crawler stop.");
    }
}

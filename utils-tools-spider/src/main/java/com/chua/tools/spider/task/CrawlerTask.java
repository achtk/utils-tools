package com.chua.tools.spider.task;

import com.chua.tools.spider.config.CrawlerConf;
import com.chua.tools.spider.crawler.Crawler;
import com.chua.tools.spider.parser.ApiParser;
import com.chua.tools.spider.parser.Parser;
import com.chua.tools.spider.process.ParserProcessor;
import com.chua.tools.spider.request.PageRequest;
import com.chua.tools.spider.util.UrlUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 爬虫任务
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Slf4j
public class CrawlerTask implements Task {

    private Crawler crawler;
    private AtomicBoolean running = new AtomicBoolean(false);

    public CrawlerTask(Crawler crawler) {
        this.crawler = crawler;
        this.running.set(true);
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                String url = crawler.getUrlLoader().getUrl();
                if (log.isDebugEnabled()) {
                    log.info(">>>>>>>>>>> crawler, process url : {}", url);
                }
                if (!UrlUtil.isUrl(url)) {
                    continue;
                }
                CrawlerConf crawlerConf = crawler.getCrawlerConf();
                // 页面解析器
                List<Parser> pageParser = crawlerConf.getParser();
                //解析器信息解析
                List<ParserProcessor> processors = crawlerConf.getProcessors();
                // 失败尝试
                for (int i = 0; i < (1 + crawler.getCrawlerConf().getFailRetryCount()); i++) {
                    boolean ret = false;
                    try {
                        PageRequest pageRequest = UrlUtil.createPageRequest(crawler, url);
                        for (Parser parser : pageParser) {
                            //预处理
                            parser.pretreatment(pageRequest);
                            for (ParserProcessor processor : processors) {
                                if (processor.matcher(parser)) {
                                    try {
                                        ret = processor.processor(crawler, pageRequest);
                                        break;
                                    } catch (IOException e) {
                                        continue;
                                    }
                                }
                            }

                        }

                    } catch (Throwable e) {
                        log.info(">>>>>>>>>>> crawler proocess error.", e);
                    }

                    if (crawlerConf.getPauseMillis() > 0) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(crawlerConf.getPauseMillis());
                        } catch (InterruptedException e) {
                            log.info(">>>>>>>>>>> crawler thread is interrupted. {}", e.getMessage());
                        }
                    }
                    if (ret) {
                        break;
                    }
                }

            } catch (Throwable e) {
                if (e instanceof InterruptedException) {
                    log.info(">>>>>>>>>>> crawler thread is interrupted. {}", e.getMessage());
                } else {
                    log.error(e.getMessage(), e);
                }
            }

        }
    }

    @Override
    public void toStop() {
        this.running.set(false);
    }
}

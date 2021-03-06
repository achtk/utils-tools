package com.chua.utils.tools.spider.factory;

import com.chua.utils.tools.spider.config.ProcessConfiguration;
import com.chua.utils.tools.spider.config.SpiderConfig;
import com.chua.utils.tools.spider.config.scheduler.SpikeFileCacheQueueScheduler;
import com.chua.utils.tools.spider.interpreter.IPageInterpreter;
import com.chua.utils.tools.spider.process.PageProcessor;
import com.chua.utils.tools.spider.process.WebMagicProcessor;
import lombok.AllArgsConstructor;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 爬虫工厂
 *
 * @author CH
 */
@AllArgsConstructor
public class SpiderFactory {

    private SpiderConfig spiderConfig;
    private Set<IPageInterpreter> interpreters;
    private Set<String> urls;
    private int threadNum = Runtime.getRuntime().availableProcessors() * 2;
    private Scheduler scheduler;

    public static SpiderFactory.Builder newBuilder() {
        return new SpiderFactory.Builder();
    }

    /**
     * 启动
     *
     * @return
     */
    public void runAsync() {
        runAsync(null);
    }

    /**
     * 启动
     *
     * @param processor 页面处理器
     */
    public void runAsync(PageProcessor processor) {
        processor = null == processor ? new WebMagicProcessor() : processor;

        ProcessConfiguration processConfiguration = new ProcessConfiguration();
        processConfiguration.setInterpreters(interpreters);
        processConfiguration.setSpiderConfig(spiderConfig);

        processor.config(processConfiguration);

        Spider spider = Spider.create((us.codecraft.webmagic.processor.PageProcessor) processor);
        spider.addUrl(urls.toArray(new String[0]));
        spider.setScheduler(scheduler);
        spider.thread(threadNum);
        spider.run();
    }


    public static class Builder {
        private Set<String> urls = new HashSet<>();
        private SpiderConfig spiderConfig = new SpiderConfig();
        private Set<IPageInterpreter> interpreters = new HashSet<>();
        private int threadNum = 3;
        private Scheduler scheduler = new SpikeFileCacheQueueScheduler(System.getProperty("user.home"), urls);

        public Builder addInterpreter(IPageInterpreter interpreter) {
            interpreters.add(interpreter);
            return this;
        }

        public Builder setUrl(String... url) {
            this.urls.clear();
            urls.addAll(Arrays.asList(url));
            return this;
        }

        public Builder addUrl(String url) {
            this.urls.add(url);
            return this;
        }

        public Builder threadNum(int threadNum) {
            this.threadNum = threadNum;
            return this;
        }

        public Builder setScheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        public SpiderFactory newFactory() {
            return new SpiderFactory(spiderConfig, interpreters, this.urls, threadNum, scheduler);
        }
    }
}

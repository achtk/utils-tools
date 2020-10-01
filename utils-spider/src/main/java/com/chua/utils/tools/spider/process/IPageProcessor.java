package com.chua.utils.tools.spider.process;

import com.chua.utils.tools.spider.config.ProcessConfiguration;

/**
 * 页面进程
 * @author CH
 */
public interface IPageProcessor<Page> {
    /**
     * 页面进程
     * @param page
     */
    void process(Page page);

    /**
     * 进程配置
     * @param processConfiguration
     */
    void config(ProcessConfiguration processConfiguration);
}

package com.chua.utils.tools.spider.process;

import com.chua.utils.tools.spider.config.ProcessConfiguration;

/**
 * 页面进程
 * @author CH
 */
public interface PageProcessor<P> {
    /**
     * 页面进程
     * @param p 页面对象
     */
    void process(P p);

    /**
     * 进程配置
     * @param processConfiguration 进程配置
     */
    void config(ProcessConfiguration processConfiguration);
}

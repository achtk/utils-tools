package com.chua.tools.spider.process;

import com.chua.tools.spider.crawler.Crawler;
import com.chua.tools.spider.parser.Parser;
import com.chua.tools.spider.request.PageRequest;

import java.io.IOException;

/**
 * 进度解析器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public interface ParserProcessor {
    /**
     * 是否处理解析器数据
     *
     * @param parser 解析器
     * @return
     */
    boolean matcher(Parser parser);

    /**
     * 解析请求
     *
     * @param crawler     爬虫
     * @param pageRequest 请求
     * @throws IOException IOException
     * @return boolean
     */
    boolean processor(Crawler crawler, PageRequest pageRequest) throws IOException;
}

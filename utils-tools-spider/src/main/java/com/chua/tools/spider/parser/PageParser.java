package com.chua.tools.spider.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 页面解析器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public interface PageParser<T> extends Parser {
    /**
     * 解析页面实体
     *
     * @param html          page html data
     * @param pageVoElement pageVo html data
     * @param pageVo        pageVo object
     */
    void parse(Document html, Element pageVoElement, T pageVo);
}

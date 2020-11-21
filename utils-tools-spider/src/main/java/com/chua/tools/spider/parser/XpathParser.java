package com.chua.tools.spider.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;

import java.util.*;

/**
 * xparser
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public interface XpathParser extends PageParser {
    /**
     * 解析
     *
     * @param html          page html data
     * @param pageVoElement pageVo html data
     * @param pageVo        pageVo object
     */
    @Override
    default void parse(Document html, Element pageVoElement, Object pageVo) {
        Set<String> xpath = xpath();
        if (null == xpath) {
            return;
        }
        Map<String, List<String>> param = new HashMap<>();
        JXDocument jxDocument = JXDocument.create(html);
        for (String s : xpath) {
            List<Object> list = jxDocument.sel(s);
            if (null == list || list.size() == 0) {
                continue;
            }
            List<String> value = new ArrayList<>(list.size());
            for (Object o : list) {
                if (o instanceof Element) {
                    value.add(((Element) o).text());
                }
            }
            param.put(s, value);
        }
        parse(html, param);
    }

    /**
     * 解析
     *
     * @param html    html
     * @param content 内容
     */
    void parse(Document html, Map<String, List<String>> content);

    /**
     * 表达式
     *
     * @return 表达式
     */
    Set<String> xpath();
}

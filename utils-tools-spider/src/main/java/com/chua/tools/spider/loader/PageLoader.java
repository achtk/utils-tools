package com.chua.tools.spider.loader;

import com.chua.tools.spider.request.PageRequest;
import org.jsoup.nodes.Document;

/**
 * 页面加载器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public interface PageLoader {
    /**
     * load page
     *
     * @param pageRequest
     * @return Document
     */
    Document load(PageRequest pageRequest);
}

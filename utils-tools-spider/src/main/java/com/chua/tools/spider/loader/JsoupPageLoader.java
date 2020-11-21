package com.chua.tools.spider.loader;

import com.chua.tools.spider.request.PageRequest;
import com.chua.tools.spider.util.JsoupUtil;
import org.jsoup.nodes.Document;

/**
 * jsoup加载
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class JsoupPageLoader implements PageLoader {
    @Override
    public Document load(PageRequest pageRequest) {
        return JsoupUtil.load(pageRequest);
    }
}

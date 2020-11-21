package com.chua.tools.spider.process;

import com.chua.tools.spider.crawler.Crawler;
import com.chua.tools.spider.parser.ApiParser;
import com.chua.tools.spider.parser.Parser;
import com.chua.tools.spider.request.PageRequest;
import com.chua.tools.spider.util.JsoupUtil;

import java.io.IOException;

/**
 * api加载器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class ApiParserProcessor implements ParserProcessor {

    private ApiParser parser;

    @Override
    public boolean matcher(Parser parser) {
        if (parser instanceof ApiParser) {
            this.parser = (ApiParser) parser;
            return true;
        }
        return false;
    }

    @Override
    public boolean processor(Crawler crawler, PageRequest pageRequest) throws IOException {
        String pareses = JsoupUtil.loadPageSource(pageRequest);
        if (pareses == null) {
            return false;
        }
        parser.parse(pageRequest.getUrl(), pareses);
        return true;
    }
}

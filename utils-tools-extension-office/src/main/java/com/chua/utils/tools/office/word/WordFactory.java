package com.chua.utils.tools.office.word;

import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.office.word.docx.WordDocExtractor;
import com.chua.utils.tools.office.word.docx.WordDocxExtractor;
import com.google.common.collect.Lists;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * word工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public class WordFactory {

    private static final List<DocExtractor> EXTRACTORS = Lists.newArrayList(
            new WordDocxExtractor(),
            new WordDocExtractor());

    /**
     * 解析word
     *
     * @param matcher 匹配器
     * @param url     url
     */
    public void reader(Matcher<String> matcher, URL url) throws Exception {
        if (null == matcher) {
            return;
        }
        ExecutorService executorService = ThreadHelper.newSingleThreadExecutor();
        executorService.execute(() -> {
            for (DocExtractor extractor : EXTRACTORS) {
                if (extractor.matcher(url)) {
                    try {
                        String analyse = extractor.analyse();
                        matcher.doWith(analyse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            executorService.shutdownNow();
        });
    }
}

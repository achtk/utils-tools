package com.chua.utils.tools.office.word;

import java.io.IOException;
import java.net.URL;

/**
 * doc解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public interface DocExtractor {
    /**
     * 是否匹配
     *
     * @param url url
     * @return boolean
     */
    boolean matcher(URL url);

    /**
     * 解析
     * @return
     */
    String analyse() throws IOException;
}

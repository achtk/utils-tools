package com.chua.utils.tools.resource.parser;

import com.chua.utils.tools.common.IoHelper;
import com.chua.utils.tools.function.Matcher;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * 解析器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public interface Parser {
    /**
     * 是否匹配
     *
     * @param url url
     * @return 匹配返回true
     */
    boolean matcher(URL url);

    /**
     * 文件
     *
     * @param url     文件
     * @param matcher 匹配器
     * @return ParserDir
     * @throws Exception Exception
     */
    ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception;
}

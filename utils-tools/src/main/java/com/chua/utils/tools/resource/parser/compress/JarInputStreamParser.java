package com.chua.utils.tools.resource.parser.compress;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.Parser;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.dir.JarInputParserDir;

import java.net.URL;

/**
 * jarInputStream
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class JarInputStreamParser implements Parser {
    @Override
    public boolean matcher(URL url) {
        return url.toExternalForm().contains(".jar");
    }

    @Override
    public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
        return new JarInputParserDir(url, matcher);
    }
}

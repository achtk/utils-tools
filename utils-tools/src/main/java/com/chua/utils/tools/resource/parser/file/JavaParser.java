package com.chua.utils.tools.resource.parser.file;


import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.constant.SuffixConstant;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.Parser;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.dir.JavaFileParserDir;
import com.chua.utils.tools.resource.parser.compress.dir.SystemFileParserDir;

import java.io.File;
import java.net.URL;

import static com.chua.utils.tools.constant.SuffixConstant.SUFFIX_JAVA;

/**
 * class解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class JavaParser implements Parser {
    @Override
    public boolean matcher(URL url) {
        return null != url && url.toExternalForm().endsWith(SUFFIX_JAVA);
    }

    @Override
    public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
        return new JavaFileParserDir(new File(url.getFile()), matcher);
    }
}

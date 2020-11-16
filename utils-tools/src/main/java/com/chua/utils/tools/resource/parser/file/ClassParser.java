package com.chua.utils.tools.resource.parser.file;


import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.Parser;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.dir.ClassFileParserDir;
import com.chua.utils.tools.resource.parser.compress.dir.SystemFileParserDir;
import com.chua.utils.tools.resource.parser.compress.dir.SystemParserDir;

import java.io.File;
import java.net.URL;

/**
 * class解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class ClassParser implements Parser {
    @Override
    public boolean matcher(URL url) {
        return null != url && url.toExternalForm().endsWith(StringConstant.CLASS_FILE_EXTENSION);
    }

    @Override
    public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
        return new ClassFileParserDir(new File(url.getFile()), matcher);
    }
}

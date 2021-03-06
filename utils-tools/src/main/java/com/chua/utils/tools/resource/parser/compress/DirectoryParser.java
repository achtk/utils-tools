package com.chua.utils.tools.resource.parser.compress;

import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.Parser;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.dir.SystemParserDir;

import java.io.IOException;
import java.net.URL;

import static com.chua.utils.tools.constant.StringConstant.FILE;
import static com.chua.utils.tools.constant.StringConstant.JAR_PATTERN;

/**
 * 文件夹解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class DirectoryParser implements Parser {
    @Override
    public boolean matcher(URL url) {
        if (FILE.equals(url.getProtocol()) && !url.toExternalForm().matches(JAR_PATTERN)) {
            java.io.File file = FileHelper.tryFile(url);
            return file != null && file.isDirectory();
        }
        return false;
    }

    @Override
    public ParserDir path(URL url, Matcher<ParserFile> matcher) throws IOException {
        return new SystemParserDir(FileHelper.tryFile(url), matcher);
    }
}

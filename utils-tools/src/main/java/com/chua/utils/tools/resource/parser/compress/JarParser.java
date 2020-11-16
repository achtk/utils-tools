package com.chua.utils.tools.resource.parser.compress;

import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.Parser;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.dir.ZipParserDir;

import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

/**
 * jar解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class JarParser implements Parser {

    @Override
    public boolean matcher(URL url) {
        return null != url && url.getProtocol().equals("file") && url.toExternalForm().matches(".*\\.jar(\\!.*|$)");
    }

    @Override
    public ParserDir path(URL url, Matcher<ParserFile> matcher) throws IOException {
        return new ZipParserDir(new JarFile(FileHelper.tryFile(url)), matcher);
    }


}

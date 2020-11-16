package com.chua.utils.tools.resource.parser.compress;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.Parser;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.vfs.UrlTypeVfs;

import java.net.URL;

import static com.chua.utils.tools.constant.StringConstant.URL_PROTOCOL_VFSFILE;
import static com.chua.utils.tools.constant.StringConstant.URL_PROTOCOL_VFSZIP;

/**
 * Jboss vfs file
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class JbossVfsFileParser implements Parser {
    @Override
    public boolean matcher(URL url) {
        return URL_PROTOCOL_VFSZIP.equals(url.getProtocol()) || URL_PROTOCOL_VFSFILE.equals(url.getProtocol());
    }

    @Override
    public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
        return new UrlTypeVfs().createDir(url, matcher);
    }
}

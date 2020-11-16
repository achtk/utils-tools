package com.chua.utils.tools.resource.parser.compress;

import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.Parser;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.dir.ZipParserDir;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarFile;

import static com.chua.utils.tools.constant.StringConstant.*;

/**
 * jar解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class JarUrlParser implements Parser {

    @Override
    public boolean matcher(URL url) {
        return JAR.equals(url.getProtocol())
                || ZIP.equals(url.getProtocol())
                || URL_PROTOCOL_WSJAR.equals(url.getProtocol());
    }

    @Override
    public ParserDir path(URL url, Matcher<ParserFile> matcher) throws IOException {
        try {
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof JarURLConnection) {
                urlConnection.setUseCaches(false);
                return new ZipParserDir(((JarURLConnection) urlConnection).getJarFile(), matcher);
            }
        } catch (Throwable ignore) {
        }
        java.io.File file = FileHelper.tryFile(url);
        if (file != null) {
            return new ZipParserDir(new JarFile(file), matcher);
        }
        return null;
    }
}

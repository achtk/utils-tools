package com.chua.utils.tools.resource.parser.compress;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.Parser;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.vfs.ParserVfs;
import org.reflections.util.ClasspathHelper;

import java.net.URL;

/**
 * bundle 解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class BundleParser implements Parser {
    @Override
    public boolean matcher(URL url) {
        return url.getProtocol().startsWith("bundle");
    }

    @Override
    public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
        return ParserVfs.fromCompressUrl((URL) ClasspathHelper.contextClassLoader().
                loadClass("org.eclipse.core.runtime.FileLocator")
                .getMethod("resolve", URL.class)
                .invoke(null, url), matcher);

    }
}

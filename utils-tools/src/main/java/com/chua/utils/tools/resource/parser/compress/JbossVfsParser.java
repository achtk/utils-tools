package com.chua.utils.tools.resource.parser.compress;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.Parser;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.dir.SystemParserDir;
import com.chua.utils.tools.resource.parser.compress.dir.ZipParserDir;
import org.reflections.util.ClasspathHelper;

import java.net.URL;
import java.util.jar.JarFile;

/**
 * jboss vfs
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class JbossVfsParser implements Parser {
    @Override
    public boolean matcher(URL url) {
        return "vfs".equals(url.getProtocol());
    }

    @Override
    public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
        Object content = url.openConnection().getContent();
        Class<?> virtualFile = ClasspathHelper.contextClassLoader().loadClass("org.jboss.vfs.VirtualFile");
        java.io.File physicalFile = (java.io.File) virtualFile.getMethod("getPhysicalFile").invoke(content);
        String name = (String) virtualFile.getMethod("getName").invoke(content);
        java.io.File file = new java.io.File(physicalFile.getParentFile(), name);
        if (!file.exists() || !file.canRead()) {
            file = physicalFile;
        }
        return file.isDirectory() ? new SystemParserDir(file, matcher) : new ZipParserDir(new JarFile(file), matcher);
    }
}

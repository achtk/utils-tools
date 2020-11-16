package com.chua.utils.tools.resource.parser.compress.dir;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.log.Log;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.file.ZipParserFile;

import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * ZIP压缩包解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class ZipParserDir implements ParserDir {

    public final java.util.zip.ZipFile jarFile;
    private final Matcher<ParserFile> matcher;

    public ZipParserDir(ZipFile jarFile, Matcher<ParserFile> matcher) {
        this.jarFile = jarFile;
        this.matcher = matcher;
    }

    @Override
    public String getPath() {
        return jarFile.getName();
    }

    @Override
    public Iterable<ParserFile> getFiles() {
        return () -> jarFile.stream()
                .filter(entry -> !entry.isDirectory())
                .map(entry -> {
                    ParserFile parserFile = new ZipParserFile(ZipParserDir.this, entry);
                    Matcher.doWith(matcher, parserFile);
                    return parserFile;
                })
                .iterator();
    }

    @Override
    public void close() {
        try {
            jarFile.close();
        } catch (IOException e) {
            Log.log.warn("Could not close JarFile", e);
        }
    }

    @Override
    public String toString() {
        return jarFile.getName();
    }
}

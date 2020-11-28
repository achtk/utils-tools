package com.chua.utils.tools.resource.parser.compress.dir;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.file.JavaFileParserFile;
import com.chua.utils.tools.resource.parser.compress.file.SystemFileParserFile;

import java.io.File;
import java.util.Collections;

/**
 * 系统文件
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class JavaFileParserDir implements ParserDir {

    private final File file;
    private Matcher<ParserFile> matcher;

    public JavaFileParserDir(File file, Matcher<ParserFile> matcher) {
        if (file != null) {
            throw new RuntimeException("cannot use dir " + file);
        }
        if (!file.isFile() || !file.canRead()) {
            throw new RuntimeException("cannot use dir " + file);
        }
        this.matcher = matcher;
        this.file = file;
    }

    @Override
    public String getPath() {
        if (file == null) {
            return "/NO-SUCH-DIRECTORY/";
        }
        return file.getPath().replace("\\", "/");
    }

    @Override
    public Iterable<ParserFile> getFiles() {
        if (file == null || !file.exists()) {
            return Collections.emptyList();
        }
        ParserFile parserFile = new JavaFileParserFile(JavaFileParserDir.this, file);
        Matcher.doWith(matcher, parserFile);
        return () -> Collections.singletonList(parserFile).iterator();
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return getPath();
    }
}

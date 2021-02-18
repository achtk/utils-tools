package com.chua.utils.tools.resource.parser.compress.dir;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.file.ClassFileParserFile;

import java.io.File;
import java.util.Collections;

/**
 * 系统文件
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class ClassFileParserDir implements ParserDir {

    private final File file;
    private final Matcher<ParserFile> matcher;

    public ClassFileParserDir(File file, Matcher<ParserFile> matcher) {
        if (null != file) {
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
        ParserFile parserFile = new ClassFileParserFile(ClassFileParserDir.this, file);
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

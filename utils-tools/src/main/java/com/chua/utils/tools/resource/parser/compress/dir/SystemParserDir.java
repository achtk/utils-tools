package com.chua.utils.tools.resource.parser.compress.dir;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.file.SystemParserFile;
import org.reflections.ReflectionsException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

/**
 * 系统文件解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class SystemParserDir implements ParserDir {

    private final File file;
    private Matcher<ParserFile> matcher;

    public SystemParserDir(File file, Matcher<ParserFile> matcher) {
        if (file != null && (!file.isDirectory() || !file.canRead())) {
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
        return () -> {
            try {
                return Files.walk(file.toPath())
                        .filter(Files::isRegularFile)
                        .map(path -> {
                            ParserFile parserFile = new SystemParserFile(SystemParserDir.this, path.toFile());
                            Matcher.doWith(matcher, parserFile);
                            return parserFile;
                        })
                        .iterator();
            } catch (IOException e) {
                throw new ReflectionsException("could not get files for " + file, e);
            }
        };
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return getPath();
    }
}

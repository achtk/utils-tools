package com.chua.utils.tools.resource.parser.compress.file;

import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.dir.JarInputParserDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;

/**
 * JarInputParserFile
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class JarInputParserFile implements ParserFile {

    private final ZipEntry entry;
    private final JarInputParserDir jarInputDir;
    private final long fromIndex;
    private final long endIndex;

    public JarInputParserFile(ZipEntry entry, JarInputParserDir jarInputDir, long cursor, long nextCursor) {
        this.entry = entry;
        this.jarInputDir = jarInputDir;
        fromIndex = cursor;
        endIndex = nextCursor;
    }

    @Override
    public String getName() {
        String name = entry.getName();
        return name.substring(name.lastIndexOf("/") + 1);
    }

    @Override
    public String getRelativePath() {
        return entry.getName();
    }

    @Override
    public Path path() {
        return Paths.get(entry.getName());
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                if (jarInputDir.cursor >= fromIndex && jarInputDir.cursor <= endIndex) {
                    int read = jarInputDir.jarInputStream.read();
                    jarInputDir.cursor++;
                    return read;
                } else {
                    return -1;
                }
            }
        };
    }
}

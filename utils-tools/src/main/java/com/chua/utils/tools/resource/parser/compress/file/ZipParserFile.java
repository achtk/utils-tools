package com.chua.utils.tools.resource.parser.compress.file;

import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.dir.ZipParserDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;

/**
 * zip文件
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class ZipParserFile implements ParserFile {

    private ZipParserDir zipParserDir;
    private ZipEntry zipEntry;

    public ZipParserFile(final ZipParserDir root, ZipEntry entry) {
        this.zipParserDir = root;
        this.zipEntry = entry;
    }

    @Override
    public String getName() {
        String name = zipEntry.getName();
        return name.substring(name.lastIndexOf("/") + 1);
    }

    @Override
    public String getRelativePath() {
        return zipEntry.getName();
    }

    @Override
    public Path path() {
        return Paths.get(zipEntry.getName());
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return zipParserDir.jarFile.getInputStream(zipEntry);
    }

    @Override
    public String toString() {
        return zipParserDir.getPath() + "!" + java.io.File.separatorChar + zipEntry.toString();
    }
}

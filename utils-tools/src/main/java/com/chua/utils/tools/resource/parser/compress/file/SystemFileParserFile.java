package com.chua.utils.tools.resource.parser.compress.file;

import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.compress.dir.SystemFileParserDir;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 系统文件
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class SystemFileParserFile implements ParserFile {

    private final SystemFileParserDir root;
    private final java.io.File file;

    public SystemFileParserFile(final SystemFileParserDir root, java.io.File file) {
        this.root = root;
        this.file = file;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getRelativePath() {
        String filepath = file.getPath().replace("\\", "/");
        if (filepath.startsWith(root.getPath())) {
            int length = root.getPath().length() + 1;
            if(length < filepath.length()) {
                return filepath.substring(length);
            }
            return filepath;
        }

        return null;
    }

    @Override
    public Path path() {
        return Paths.get(file.getPath());
    }

    @Override
    public InputStream openInputStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return file.toString();
    }
}

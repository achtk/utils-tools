package com.chua.utils.tools.office.word.parser.file;

import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.office.word.WordFactory;
import com.chua.utils.tools.office.word.parser.dir.WordFileParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.ParserProcess;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * xls文件解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
@Slf4j
public class WordFileParserFile implements ParserFile, ParserProcess<Map<String, String>> {

    private final WordFileParserDir root;
    private final java.io.File file;

    public WordFileParserFile(final WordFileParserDir root, java.io.File file) {
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
            if (length < filepath.length()) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return file.toString();
    }

    @Override
    public void doWith(Matcher<Map<String, String>> matcher) {

        WordFactory wordFactory = new WordFactory();
        try {
            //wordFactory.reader(matcher, file.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.chua.utils.tools.office.word.parser.dir;

import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.constant.SuffixConstant;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.office.word.parser.file.WordFileParserFile;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * xls解析目录
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public class WordFileParserDir implements ParserDir {

    private final File file;
    private final Matcher<ParserFile> matcher;

    public WordFileParserDir(File file, Matcher<ParserFile> matcher) {
        this.matcher = matcher;
        this.file = file;
    }

    @Override
    public String getPath() {
        return file.getPath().replace("\\", "/");
    }


    @Override
    public Iterable<ParserFile> getFiles() {
        if (file == null || !file.exists()) {
            return Collections.emptyList();
        }
        List<ParserFile> parserFileList = new ArrayList<>();
        if (file.isFile()) {
            ParserFile parserFile = new WordFileParserFile(WordFileParserDir.this, file);
            Matcher.doWith(matcher, parserFile);
            parserFileList.add(parserFile);
        } else {
            FileHelper.doWith(file.getAbsolutePath(), path -> {
                if (!path.getFileName().toString().endsWith(SuffixConstant.SUFFIX_CSV)) {
                    return;
                }
                ParserFile parserFile = new WordFileParserFile(WordFileParserDir.this, path.toFile());
                Matcher.doWith(matcher, parserFile);
                parserFileList.add(parserFile);
            });
        }
        return () -> parserFileList.iterator();
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return getPath();
    }
}

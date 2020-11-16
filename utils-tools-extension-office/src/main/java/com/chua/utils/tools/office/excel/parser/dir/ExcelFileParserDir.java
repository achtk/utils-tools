package com.chua.utils.tools.office.excel.parser.dir;

import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.constant.SuffixConstant;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.office.csv.parser.dir.CsvFileParserDir;
import com.chua.utils.tools.office.csv.parser.file.CsvFileParserFile;
import com.chua.utils.tools.office.excel.parser.file.ExcelFileParserFile;
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
public class ExcelFileParserDir implements ParserDir {

    private final File file;
    private Matcher<ParserFile> matcher;

    public ExcelFileParserDir(File file, Matcher<ParserFile> matcher) {
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
            ParserFile parserFile = new ExcelFileParserFile(ExcelFileParserDir.this, file);
            Matcher.doWith(matcher, parserFile);
            parserFileList.add(parserFile);
        } else {
            FileHelper.doWith(file.getAbsolutePath(), path -> {
                String name = path.getFileName().toString();
                if (!name.endsWith(SuffixConstant.SUFFIX_XLSX) || !name.endsWith(SuffixConstant.SUFFIX_XLS)) {
                    return;
                }
                ParserFile parserFile = new ExcelFileParserFile(ExcelFileParserDir.this, path.toFile());
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

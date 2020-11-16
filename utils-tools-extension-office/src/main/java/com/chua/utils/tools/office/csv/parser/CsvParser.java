package com.chua.utils.tools.office.csv.parser;

import com.chua.utils.tools.constant.SuffixConstant;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.office.csv.parser.dir.CsvFileParserDir;
import com.chua.utils.tools.office.excel.parser.dir.ExcelFileParserDir;
import com.chua.utils.tools.resource.parser.Parser;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;

import java.io.File;
import java.net.URL;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public class CsvParser implements Parser {
    @Override
    public boolean matcher(URL url) {
        return null != url && (url.toExternalForm().endsWith(SuffixConstant.SUFFIX_XLS) || new File(url.getFile()).isDirectory());
    }

    @Override
    public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
        return new CsvFileParserDir(new File(url.getFile()), matcher);
    }
}

package com.chua.utils.tools.office.word.parser;

import com.chua.utils.tools.constant.SuffixConstant;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.office.word.parser.dir.WordFileParserDir;
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
public class WordParser implements Parser {
    @Override
    public boolean matcher(URL url) {
        return null != url && (
                url.toExternalForm().endsWith(SuffixConstant.SUFFIX_DOC) ||
                        url.toExternalForm().endsWith(SuffixConstant.SUFFIX_DOCX) ||
                        new File(url.getFile()).isDirectory());
    }

    @Override
    public ParserDir path(URL url, Matcher<ParserFile> matcher) throws Exception {
        return new WordFileParserDir(new File(url.getFile()), matcher);
    }
}

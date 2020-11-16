package com.chua.utils.tools.office.csv.parser.file;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.chua.utils.tools.constant.SuffixConstant;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.office.csv.CsvFactory;
import com.chua.utils.tools.office.csv.parser.dir.CsvFileParserDir;
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
public class CsvFileParserFile implements ParserFile, ParserProcess<Map<String, String>> {

    private final CsvFileParserDir root;
    private final java.io.File file;

    public CsvFileParserFile(final CsvFileParserDir root, java.io.File file) {
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
        CsvFactory csvFactory = new CsvFactory();
        try {
            csvFactory.reader(matcher, file.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件类型
     *
     * @return 文件类型
     */
    private ExcelTypeEnum getExcelType() {
        String name = getName();

        if (name.equals(SuffixConstant.SUFFIX_XLS)) {
            return ExcelTypeEnum.XLS;
        }

        if (name.equals(SuffixConstant.SUFFIX_XLSX)) {
            return ExcelTypeEnum.XLSX;
        }
        return null;
    }
}

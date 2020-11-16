package com.chua.utils.tools.office.csv;

import com.chua.utils.tools.function.Matcher;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * csv工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
@AllArgsConstructor
@NoArgsConstructor
public class CsvFactory {
    /**
     * 设置跳过 header
     */
    private boolean isSkipHeader;
    /**
     * 分隔符
     */
    private final char delimiter = '\t';
    /**
     * csv格式
     */
    private final CSVFormat csvFormat = CSVFormat.DEFAULT;


    /**
     * 读取文件
     *
     * @param matcher 匹配器
     * @param url     url
     */
    public void reader(Matcher<Map<String, String>> matcher, URL url) throws Exception {
        CSVFormat csvFormat = createCsvFormat();
        // 将文件转换为 Reader 流
        Reader in = new FileReader(new File(url.getFile()));
        // 解析数据
        CSVParser parser = csvFormat.parse(in);
        // 获取记录
        List<CSVRecord> records = parser.getRecords();

        records.parallelStream().forEach(record -> {
            Map<String, String> map = record.toMap();
            try {
                matcher.doWith(map);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * 创建csvFormat
     *
     * @return CSVFormat
     */
    private CSVFormat createCsvFormat() {
        if (isSkipHeader) {
            csvFormat.withSkipHeaderRecord();
        }

        csvFormat.withDelimiter(delimiter);

        return csvFormat;
    }
}

package com.chua.utils.tools.office.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.chua.utils.tools.function.FileConverter;
import com.chua.utils.tools.prop.mapper.FileMapper;
import com.chua.utils.tools.prop.resolver.IFileResolver;
import com.google.common.collect.HashMultimap;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * xls|csv解析
 * @author CH
 */
public class XlsFileResolver implements IFileResolver {

    private final HashMultimap<String, Map<Object, Object>> items = HashMultimap.create();

    @Override
    public void stream(InputStream inputStream) {

        ExcelReaderBuilder readerBuilder = EasyExcelFactory.read(inputStream, new AnalysisEventListener<LinkedHashMap<Object, Object>>() {

            @Override
            public void invoke(LinkedHashMap<Object, Object> data, AnalysisContext context) {
                items.put(context.readSheetHolder().getSheetName(), data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
            }
        });
        ExcelReader excelReader = readerBuilder.build();
        excelReader.readAll();
    }

    @Override
    public FileMapper analysis(FileConverter fileConverter) {
        if(null == items) {
            return null;
        }
        FileMapper fileMapper = new FileMapper();
        fileMapper.setHashMultimap(items);
        return fileMapper;
    }

    @Override
    public String[] suffixes() {
        return new String[] {"xls", "csv"};
    }
}

package com.chua.utils.tools.office.excel;

import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.prop.mapper.FileMapper;
import com.chua.utils.tools.prop.resolver.IFileResolver;
import lombok.SneakyThrows;

/**
 * @author CH
 */
public class XlsFileResolverTest {

    @SneakyThrows
    public static void main(String[] args) {
        String path = "I://1.xls";
        IFileResolver fileResolver = new XlsFileResolver();
        fileResolver.stream(FileHelper.toInputStream(path));
        FileMapper fileMapper = fileResolver.analysis(null);
        System.out.println();
    }

}
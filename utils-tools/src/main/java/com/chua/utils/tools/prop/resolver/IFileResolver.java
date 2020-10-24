package com.chua.utils.tools.prop.resolver;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.prop.mapper.FileMapper;
import com.chua.utils.tools.function.AbstractConverter;
import com.google.common.base.Strings;

import java.io.InputStream;

/**
 * 文件解析
 * @author CH
 */
public interface IFileResolver {
    /**
     * 设置流
     * @param inputStream 流
     */
    void stream(InputStream inputStream);

    /**
     * 解析文件
     * @param abstractConverter 转化器
     * @return
     */
    FileMapper analysis(AbstractConverter abstractConverter);

    /**
     * 支持的文件后缀
     * @return
     */
    String[] suffixes();
    /**
     * 是否匹配
     * @param extension
     * @return
     */
    default boolean isValid(String extension) {
        if(Strings.isNullOrEmpty(extension) || !BooleanHelper.hasLength(suffixes())) {
            return false;
        }

        extension = extension.toLowerCase();
        for (String suffix : suffixes()) {
            if(extension.equals(suffix)) {
                return true;
            }
        }
        return false;
    }
}

package com.chua.utils.tools.prop.loader;

import java.io.InputStream;
import java.util.Properties;

/**
 * properties数据加载器
 *
 * @author CH
 */
public interface ProfileLoader {
    /**
     * 支持的后缀
     *
     * @return
     */
    String[] suffix();

    /**
     * 解析器
     * <p>数据为properties文件格式</p>
     *
     * @param inputStream 数据
     * @return
     */
    Properties toProp(InputStream inputStream);
}

package com.chua.utils.tools.prop.placeholder.resolver;

import com.chua.utils.tools.prop.placeholder.mapper.PlaceholderMapper;

/**
 * 配置项占位符
 * @author CH
 */
interface PropertiesPlaceholderResolver {
    /**
     * 占位符前缀
     * @return
     */
    String before();

    /**
     * 分隔符
     * @return
     */
    String valueSeparate();
    /**
     * 占位符后缀
     * @return
     */
    String after();

    /**
     * 允许
     * @param value 值
     * @return
     */
    boolean isMatcher(String value);

    /**
     * 占位
     * @param value 值
     * @return
     */
    PlaceholderMapper analyze(String value);
}

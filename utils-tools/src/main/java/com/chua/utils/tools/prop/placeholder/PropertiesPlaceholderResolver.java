package com.chua.utils.tools.prop.placeholder;

import com.chua.utils.tools.prop.placeholder.mapper.PlaceholderMapper;

/**
 * 配置项占位符
 * @author CH
 */
public interface PropertiesPlaceholderResolver {
    /**
     * 占位符前缀
     * @return
     */
    public String before();

    /**
     * 分隔符
     * @return
     */
    public String valueSeparate();
    /**
     * 占位符后缀
     * @return
     */
    public String after();

    /**
     * 允许
     * @param value 值
     * @return
     */
    public boolean isMatcher(String value);

    /**
     * 占位
     * @param value 值
     * @return
     */
    public PlaceholderMapper analyze(String value);
}

package com.chua.utils.tools.transform;

/**
 * 数据格式化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
public interface OperatorFormatter {
    /**
     * 格式化数据源
     *
     * @param source 数据源
     * @return 格式化数据源
     */
    String formatter(String source);
}

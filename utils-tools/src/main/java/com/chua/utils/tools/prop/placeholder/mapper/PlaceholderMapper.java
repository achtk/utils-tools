package com.chua.utils.tools.prop.placeholder.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 占位映射
 * @author CH
 */
@Data
@AllArgsConstructor
public class PlaceholderMapper {
    /**
     * 占位数据
     */
    private String value;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 是否需要再次校验占位符
     */
    private boolean checkPlaceholder;
}

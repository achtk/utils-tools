package com.chua.utils.tools.prop.mapper;

import com.google.common.collect.HashMultimap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 文件映射
 * @author CH
 */
@Getter
@Setter
@Accessors(chain = true)
public class FileMapper {
    /**
     * 文件
     */
    private String name;
    /**
     * 属性
     */
    private HashMultimap hashMultimap;
}

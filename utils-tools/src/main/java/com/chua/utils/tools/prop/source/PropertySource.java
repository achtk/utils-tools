package com.chua.utils.tools.prop.source;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 属性源
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/4
 */
@Data
public class PropertySource {
    /**
     * 配置源名称
     */
    private String name;
    /**
     * 数据
     */
    private Map<String, Object> source = new HashMap<>();

    /**
     * 添加数据
     *
     * @param key   索引
     * @param value 值
     * @return this
     */
    public PropertySource add(String key, Object value) {
        source.put(key, value);
        return this;
    }
}

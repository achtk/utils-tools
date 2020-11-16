package com.chua.utils.tools.plugins.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 配置项目
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class PluginConfiguration {
    /**
     * 路径
     */
    private String path;
    /**
     * 只读取cfg
     */
    private boolean cfg;
}

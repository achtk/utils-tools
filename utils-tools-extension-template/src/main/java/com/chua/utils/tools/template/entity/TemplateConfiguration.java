package com.chua.utils.tools.template.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 模板配置
 * @author CH
 * @version 1.0.0
 * @since 2020/5/20 10:04
 */
@Getter
@Setter
public class TemplateConfiguration {
    /**
     * 模板配置路径
     */
    private String templatePath;
    /**
     * 输出目录
     */
    private String outPath;
    /**
     * 文件
     */
    private boolean isFile = true;

}

package com.chua.utils.tools.spider.config;

import com.chua.utils.tools.spider.interpreter.IPageInterpreter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * 集成配置
 * @author CH
 */
@Data
@EqualsAndHashCode
public class ProcessConfiguration {
    /**
     * 页面解释器
     */
    private Set<IPageInterpreter> interpreters;
    /**
     * 基础信息配置
     */
    private SpiderConfig spiderConfig;
}

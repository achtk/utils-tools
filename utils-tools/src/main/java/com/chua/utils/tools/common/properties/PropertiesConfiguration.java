package com.chua.utils.tools.common.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * properties基础配置
 * @author CH
 * @date 2020-09-29
 */
@Getter
@Setter
public class PropertiesConfiguration {
    /**
     * 类型不一致的强制转换
     */
    private boolean typeInconsistentForcedConversion = true;
    /**
     * 当数据为空是否需要空集合
     */
    private boolean needEmptyCollection = false;
    /**
     * 集合忽略null
     */
    private boolean collectionIgnoresNull = true;
    /**
     * 是否允许模糊匹配
     */
    private boolean allowFuzzyMatching = true;
}

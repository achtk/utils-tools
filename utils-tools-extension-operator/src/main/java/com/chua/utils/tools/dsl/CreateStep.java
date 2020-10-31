package com.chua.utils.tools.dsl;

/**
 * 创建
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public interface CreateStep {
    /**
     * 创建表
     * @param tableName 表
     * @return
     */
    CreateConditionStep table(String tableName);
}

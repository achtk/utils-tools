package com.chua.utils.tools.dsl;

/**
 * 删除表
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public interface DropTableStep {
    /**
     * 设置表名
     *
     * @param name 表名
     * @return CreateDBStepBuilder
     */
    DropTableStepBuilder table(String name);
}

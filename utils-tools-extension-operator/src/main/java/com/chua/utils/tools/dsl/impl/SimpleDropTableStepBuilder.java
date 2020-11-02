package com.chua.utils.tools.dsl.impl;

import com.chua.utils.tools.dsl.DropTableStepBuilder;
import lombok.AllArgsConstructor;

/**
 * 简单模式删除表构造
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
@AllArgsConstructor
public class SimpleDropTableStepBuilder implements DropTableStepBuilder {
    /**
     * 表名
     */
    private String tableName;

    @Override
    public String toSql() {
        return "DROP TABLE " +tableName;
    }
}

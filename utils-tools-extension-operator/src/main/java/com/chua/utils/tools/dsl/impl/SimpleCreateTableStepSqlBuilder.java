package com.chua.utils.tools.dsl.impl;

import com.chua.utils.tools.dsl.CreateTableStepSqlBuilder;
import com.chua.utils.tools.dsl.condition.Column;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;

import java.util.Set;

/**
 * 简单的SQL创建语句
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
@AllArgsConstructor
public class SimpleCreateTableStepSqlBuilder implements CreateTableStepSqlBuilder {
    /**
     * 创建的表
     */
    private String tableName;
    /**
     * 表信息
     */
    private SimpleCreateTableConditionStep createConditionStep;

    @Override
    public String table() {
        return tableName;
    }

    @Override
    public Set<Column> columns() {
        return createConditionStep.getColumns();
    }

    @Override
    public Set<String> extraInformation() {
        return createConditionStep.getOthers();
    }

    @Override
    public String toSql() {
        StringBuffer tableSql = new StringBuffer();
        StringBuffer columnSql = new StringBuffer();
        tableSql.append("CREATE TABLE ").append(tableName);
        tableSql.append("(");
        for (Column column : columns()) {
            columnSql.append(", ").append(column.toString(createConditionStep.getCaseType()));
        }
        if (columnSql.length() > 0) {
            tableSql.append(columnSql.substring(1).trim());
        }
        tableSql.append(")");
        if (!extraInformation().isEmpty()) {
            tableSql.append(" ").append(Joiner.on(" ").join(extraInformation()));
        }
        return tableSql.toString();
    }

}
